package com.hiveTown.model.NoticeBoard;

public enum NoticeStatus {

	INVALID(0),
	DRAFT(1),
	APPROVED(2),
	PENDING(3),
	REJECTED(4),
	EXPIRED(5);
	
	private int id;

    private NoticeStatus(int id) {
       this.id = id;
    }
    
    public int getId() {
    	return this.id;
    }
    
    public static NoticeStatus get(int id) {
    	for(NoticeStatus type : NoticeStatus.values()) {
    		if (type.getId() == id) {
    			return type;
    		}
    	}
    	return NoticeStatus.INVALID;
    }
    
}
