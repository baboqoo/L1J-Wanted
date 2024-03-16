package xnetwork;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferPool {
	@SuppressWarnings("unused")
	private static final int SEND_BUFFER_SIZE	= 1024;// 8K
	private static final int RECV_BUFFER_SIZE	= 1024 << 6;// 64K
	private static final int POOL_SLOT_SIZE		= 16;// 16
	@SuppressWarnings({ "unused", "unchecked" })
	private Queue<ByteBuffer>[] _sendPool	= new ConcurrentLinkedQueue[POOL_SLOT_SIZE];
	private Queue<ByteBuffer> _recvPool		= new ConcurrentLinkedQueue<ByteBuffer>();

	private BufferPool() {
	}

	private static BufferPool _instance;

	public static BufferPool getInstance() {
		if(_instance == null){
			synchronized(BufferPool.class){
				if(_instance == null)
					_instance = new BufferPool();
			}
		}
		return _instance;
	}

	public ByteBuffer aquireBuffer(int size) {
		return ByteBuffer.allocateDirect(size);
	}

	public void releaseBuffer(ByteBuffer buffer) {
	}

	public ByteBuffer aquireRecvBuffer() {
		return ByteBuffer.allocateDirect(RECV_BUFFER_SIZE);
	}

	public void releaseRecvBuffer(ByteBuffer buffer) {
		_recvPool.add(buffer);
	}
}

