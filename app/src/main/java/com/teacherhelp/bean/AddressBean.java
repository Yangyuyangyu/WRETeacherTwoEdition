package com.teacherhelp.bean;

import java.util.ArrayList;

public class AddressBean {
	private boolean return_code;//数据请求返回状态（true:成功   false：失败）
	private String return_msg;//返回的提示信息
	
	private boolean Selected;//是否选中
	private String Text;//名称
	private String Value;//值
	
	private String Id;// 常用地址id
	private String Name;// 常用地址名
	private String MainMobile;// 常用地址联系电话
	private boolean IsDefault;// 是否是首选
	private String DetailAdress;// 常用地址详情
	
	
	private String ZipCode;//邮编
	private String Province;//省
	private String City;//市",
	private String District;//地区
	private String DetailInfo;//详细街道
	private double BDLng;//百度经度
	private double BDLat;//百度纬度
	
	private String reason;//退货原因
	
    private ArrayList<AddressBean> provinceList;//省集合
    private ArrayList<AddressBean> cityList;//市集合
    private ArrayList<AddressBean> districtList;//区集合
    private ArrayList<AddressBean> addressList;//常用地址集合
	
	public boolean isReturn_code() {
		return return_code;
	}
	public void setReturn_code(boolean return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public boolean isSelected() {
		return Selected;
	}
	public void setSelected(boolean selected) {
		Selected = selected;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public ArrayList<AddressBean> getProvinceList() {
		return provinceList;
	}
	public void setProvinceList(ArrayList<AddressBean> provinceList) {
		this.provinceList = provinceList;
	}
	public ArrayList<AddressBean> getCityList() {
		return cityList;
	}
	public void setCityList(ArrayList<AddressBean> cityList) {
		this.cityList = cityList;
	}
	public ArrayList<AddressBean> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(ArrayList<AddressBean> districtList) {
		this.districtList = districtList;
	}
	public ArrayList<AddressBean> getAddressList() {
		return addressList;
	}
	public void setAddressList(ArrayList<AddressBean> addressList) {
		this.addressList = addressList;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMainMobile() {
		return MainMobile;
	}
	public void setMainMobile(String mainMobile) {
		MainMobile = mainMobile;
	}
	public boolean isIsDefault() {
		return IsDefault;
	}
	public void setIsDefault(boolean isDefault) {
		IsDefault = isDefault;
	}
	public String getDetailAdress() {
		return DetailAdress;
	}
	public String getZipCode() {
		return ZipCode;
	}
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getDetailInfo() {
		return DetailInfo;
	}
	public void setDetailInfo(String detailInfo) {
		DetailInfo = detailInfo;
	}
	public double getBDLng() {
		return BDLng;
	}
	public void setBDLng(double bDLng) {
		BDLng = bDLng;
	}
	public double getBDLat() {
		return BDLat;
	}
	public void setBDLat(double bDLat) {
		BDLat = bDLat;
	}
	public void setDetailAdress(String detailAdress) {
		DetailAdress = detailAdress;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
