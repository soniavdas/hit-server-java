package com.hiveTown.model;
 




public enum RoleType {
	
	INVALID(0),
	ADMIN(1),
	OFFICEBEARER(2),
	ASSOCIATIONMANAGER(3),
	ASSOCIATIONEMPLOYEE(4),
	RESIDENT(5);
	
	private int id;

    private RoleType(int id) {
            this.id = id;
    }

    public int getId() {
    	return this.id;
    }
    
    private static RoleType[] allValues = values();
    public static RoleType fromOrdinal(int n) {return allValues[n];}
}