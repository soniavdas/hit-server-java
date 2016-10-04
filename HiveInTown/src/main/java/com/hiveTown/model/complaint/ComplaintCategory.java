package com.hiveTown.model.complaint;

public enum ComplaintCategory {
	

	
	INVALID(0),
	GENERAL(1),
	ELECTRICAL(2),
	PLUMBING(3),
	PESTCONTROL(4),
	HOUSEKEEPING(5),
	SECURITY(6),
	OTHERS(7);
	
	private int id;

    private ComplaintCategory(int id) {
            this.id = id;
    }
    public int getId() {
    	return this.id;
    }


}
