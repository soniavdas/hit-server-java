package com.hiveTown.model;



public enum ApartmentUsageTypeEnum {
	
	INVALID(0),
	SELF_OCCUPIED(1),
	RENTED(2),
	COMMERCIAL(3);
	
	private int id;

    private ApartmentUsageTypeEnum(int id) {
            this.id = id;
    }

    public int getId() {
    	return this.id;
    }
}
