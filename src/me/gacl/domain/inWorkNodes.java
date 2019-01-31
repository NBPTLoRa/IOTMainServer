package me.gacl.domain;

public class inWorkNodes {
	
	private String nodeID;
	private String nodeManage;
	private String nodeCreTime;
	private String nodeState;
	
	public String getNodeID() {
		return nodeID;
	}
	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}
	public String getNodeManage() {
		return nodeManage;
	}
	public void setNodeManage(String nodeManage) {
		this.nodeManage = nodeManage;
	}
	public String getNodeCreTime() {
		return nodeCreTime;
	}
	public void setNodeCreTime(String nodeCreTime) {
		this.nodeCreTime = nodeCreTime;
	}
	public String getNodeState() {
		return nodeState;
	}
	public void setNodeState(String nodeState) {
		this.nodeState = nodeState;
	}
	
    public String toString() {
        return nodeID;
    }
}
