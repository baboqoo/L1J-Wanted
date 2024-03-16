package xnetwork;

import java.io.IOException;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

final public class SelectorThread implements Runnable {
	private Selector selector;
	private final Thread selectorThread;
	private boolean closeRequested;
	private final Queue<Runnable> pendingInvocations = new ConcurrentLinkedQueue<Runnable>();

	public SelectorThread() throws IOException {
		selector		= Selector.open();
		selectorThread	= new Thread(this);
		selectorThread.start();
	}

	public void requestClose() {
		closeRequested	= true;
		selector.wakeup();
	}

	public void addChannelInterestNow(SelectableChannel channel, int interest) throws IOException {
		if(Thread.currentThread() != selectorThread)
			throw new IOException("Method can only be called from selector thread");
		SelectionKey sk = channel.keyFor(selector);
		changeKeyInterest(sk, sk.interestOps() | interest);
	}

	public void addChannelInterestLater(final SelectableChannel channel, final int interest,
			final CallbackErrorHandler errorHandler) {
		invokeLater(new Runnable() {
			public void run() {
				try {
					addChannelInterestNow(channel, interest);
				} catch (IOException e) {
					errorHandler.handleError(e);
				}
			}
		});
	}

	public void removeChannelInterestNow(SelectableChannel channel, int interest) throws IOException {
		if(Thread.currentThread() != selectorThread)
			throw new IOException("Method can only be called from selector thread");
		SelectionKey sk = channel.keyFor(selector);
		changeKeyInterest(sk, sk.interestOps() & ~interest);
	}

	public void removeChannelInterestLater(final SelectableChannel channel, final int interest,
			final CallbackErrorHandler errorHandler) {
		invokeLater(new Runnable() {
			public void run() {
				try {
					removeChannelInterestNow(channel, interest);
				} catch (IOException e) {
					errorHandler.handleError(e);
				}
			}
		});
	}

	private void changeKeyInterest(SelectionKey sk, int newInterest) throws IOException {
		try {
			sk.interestOps(newInterest);
		} catch (CancelledKeyException cke) {
			IOException ioe = new IOException("Failed to change channel interest.");
			ioe.initCause(cke);
			throw ioe;
		}
	}

	public void registerChannelLater(final SelectableChannel channel, final int selectionKeys,
			final SelectorObject handlerInfo, final CallbackErrorHandler errorHandler) {
		invokeLater(new Runnable() {
			public void run() {
				try {
					registerChannelNow(channel, selectionKeys, handlerInfo);
				} catch (IOException e) {
					errorHandler.handleError(e);
				}
			}
		});
	}

	public void registerChannelNow(SelectableChannel channel, int selectionKeys, SelectorObject handlerInfo)throws IOException {
		if(Thread.currentThread() != selectorThread)
			throw new IOException("Method can only be called from selector thread");
		if(!channel.isOpen())
			throw new IOException("Channel is not open.");
		try {
			if(channel.isRegistered()){
				SelectionKey sk = channel.keyFor(selector);
				assert sk != null : "Channel is already registered with other selector";
				sk.interestOps(selectionKeys);
				Object previousAttach = sk.attach(handlerInfo);
				assert previousAttach != null;
			}else{
				channel.configureBlocking(false);
				channel.register(selector, selectionKeys, handlerInfo);
			}
		} catch (Exception e) {
			IOException ioe = new IOException("Error registering channel.");
			ioe.initCause(e);
			throw ioe;
		}
	}

	public void invokeLater(Runnable task) {
		pendingInvocations.add(task);
		selector.wakeup();
	}

	public void invokeAndWait(final Runnable task) throws InterruptedException {
		if(Thread.currentThread() == selectorThread)task.run();
		else {
			final Object latch = new Object();
			synchronized (latch) {
				this.invokeLater(new Runnable() {
					public void run() {
						task.run();
						latch.notify();
					}
				});
				latch.wait();
			}
		}
	}

	private void doInvocations() {
		while(true){
			Runnable task = pendingInvocations.poll();
			if(task == null)break;
			task.run();
		}
	}

	public void run() {
		while(true){
			doInvocations();
			if(closeRequested)return;
			int selectedKeys = 0;
			try {
				selectedKeys = selector.select();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				continue;
			}
			if(selectedKeys == 0)continue;
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey sk = (SelectionKey) it.next();
				it.remove();
				try {
					if(!sk.isValid())continue;
					int readyOps = sk.readyOps();
					sk.interestOps(sk.interestOps() & ~readyOps);
					SelectorObject handler = (SelectorObject) sk.attachment();
					if(sk.isAcceptable())((Acceptor) handler).handleAccept();
					else{
						Connection rwHandler = (Connection) handler;
						if(sk.isReadable())rwHandler.handleRead();
						if(sk.isValid() && sk.isWritable())rwHandler.handleWrite();
					}
				} catch (Throwable t) {
					closeSelectorAndChannels();
					t.printStackTrace();
					return;
				}
			}
		}
	}

	private void closeSelectorAndChannels() {
		Set<SelectionKey> keys = selector.keys();
		for (Iterator<SelectionKey> iter = keys.iterator(); iter.hasNext();) {
			SelectionKey key = (SelectionKey) iter.next();
			try {
				key.channel().close();
			} catch (IOException e) {
			}
		}
		try {
			selector.close();
		} catch (IOException e) {

		}
	}
}
