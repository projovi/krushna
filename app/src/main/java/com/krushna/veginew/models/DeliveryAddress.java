package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

public class DeliveryAddress {

    @SerializedName("area")
    private Area area;

    @SerializedName("pincode")
    private int pincode;

    @SerializedName("fullname")
    private String firstname;


    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;

    public String getLongitude() {
        return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	@SerializedName("address_type")
	private int addressType;

	@SerializedName("city")
	private City city;

	@SerializedName("created_at")
	private String createdAt;



	@SerializedName("area_id")
	private int areaId;

	@SerializedName("is_default")
	private int isDefault;

	@SerializedName("lastname")
	private String lastname;

    @SerializedName("number")
    private String number;


    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("address")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("alt_number")
    private String altNumber;

    @SerializedName("landmark")
    private String landmark;

	@SerializedName("city_id")
	private int cityId;

	public Area getArea() {
		return area;
	}

	public int getPincode() {
		return pincode;
	}

	public String getFirstname() {
        String first = String.valueOf(firstname.charAt(0));
        return firstname.replace(first, first.toUpperCase());
    }

	public int getAddressType() {
		return addressType;
	}

	public City getCity() {
		return city;
	}

	public String getCreatedAt() {
		return createdAt;
	}


	public int getAreaId() {
		return areaId;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public String getLastname() {
		return lastname;
	}

	public String getNumber() {
		return number;
	}


	public String getUpdatedAt() {
		return updatedAt;
	}

	public int getUserId() {
		return userId;
	}


	public int getId() {
		return id;
	}

	public String getAltNumber() {
		return altNumber;
	}

	public String getLandmark() {
		return landmark;
	}

	public int getCityId() {
		return cityId;
	}

	public static class City {

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("name")
		private String name;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("id")
		private int id;

		public String getUpdatedAt() {
			return updatedAt;
		}

		public String getName() {
			return name;
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public int getId() {
			return id;
		}
	}

	public static class Area {

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("name")
		private String name;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("id")
		private int id;

		@SerializedName("city_id")
		private int cityId;

		public String getUpdatedAt() {
			return updatedAt;
		}

		public String getName() {
			return name;
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public int getId() {
			return id;
		}

		public int getCityId() {
			return cityId;
		}
	}
}