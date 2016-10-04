package com.hiveTown.model;



public enum ResidentEnumType {
	INVALID(0),
	OWNER(1),
	TENANT(2),
	OTHER(3);
	
	private int id;

    private ResidentEnumType(int id) {
            this.id = id;
    }

    public int getId() {
    	return this.id;
    }
}
