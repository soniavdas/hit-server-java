package com.hiveTown.model.NoticeBoard;

public enum NoticeCategory {
	
	INVALID(0),
	ALL(1),
	MAINTENANCE(2),
	EVENT(3),
	MEETING(4);
	
	private int id;

    private NoticeCategory(int id) {
            this.id = id;
    }
    public int getId() {
    	return this.id;
    }
    
    public static NoticeCategory get(int id) {
    	for(NoticeCategory type : NoticeCategory.values()) {
    		if (type.getId() == id) {
    			return type;
    		}
    	}
    	return NoticeCategory.INVALID;
    }
}
