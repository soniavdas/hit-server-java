package com.hiveTown.model.complaint;

public enum ComplaintStatus {
	
	INVALID(0),
	NEW(1),
	ASSIGNED(2),
	INPROGRESS(3),
	COMPLETED(4);
	
	private int id;

    private ComplaintStatus(int id) {
            this.id = id;
    }
    public int getId() {
    	return this.id;
    }
}
