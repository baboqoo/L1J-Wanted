package l1j.server.web.dispatcher.response.gm;

public class GMServerModel {
	public int cpu_usage;
	public int thread_count;
	public int total_memory;
	public int free_memory;
	public int used_memory;
	public int max_memory;
	public ResourceThreadModel executor;
	public ResourceThreadModel scheduler;
	public ResourceThreadModel pcScheduler;
	public int clients;
	public int clientsLimit;
	public int npcs;
	public int items;
	
	public GMServerModel() {
		executor	= new ResourceThreadModel();
		scheduler	= new ResourceThreadModel();
		pcScheduler	= new ResourceThreadModel();
	}
	
	public static class ResourceThreadModel{
		public int active;
		public int poolSize;
		public int queue;
		public long task;
		public long complete;
	}
}

