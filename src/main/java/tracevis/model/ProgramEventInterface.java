package tracevis.model;

public interface ProgramEventInterface {

	void handleVMDeath(long timeStamp);

	void handleVMInit(long timeStamp);

	void handleVMStart(long timeStamp);

	void handleObjectFree(long timeStamp, String className,
			long objectID);

	void handleObjectAllocation(long timeStamp, String className,
			long objectID);

	void handleClassLoad(long timeStamp, String className);

	void handleThreadStop(long timeStamp, long threadID);

	void handleThreadStart(long timeStamp, long threadID);

	void handleFramePop(long timeStamp, long threadID,
			String className, String methodName);

	void handleMethodExit(long timeStamp, long threadID,
			String className, String methodName);

	void handleMethodEntry(long timeStamp, long threadID,
			String className, String methodName, long objectID);

}
