package com.developers.vmrapp.models;


import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public
class MarketSearch {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public MarketSearch withData(List<Datum> data) {
        this.data = data;
        return this;
    }
    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("partimage")
        @Expose
        private String partimage;
        @SerializedName("partname")
        @Expose
        private String partname;
        @SerializedName("partdescription")
        @Expose
        private String partdescription;
        @SerializedName("makemodel")
        @Expose
        private String makemodel;
        @SerializedName("condition")
        @Expose
        private String condition;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("pemissiontocontact")
        @Expose
        private Boolean pemissiontocontact;
        @SerializedName("sold")
        @Expose
        private Boolean sold;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("created")
        @Expose
        private String created;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Datum withId(Integer id) {
            this.id = id;
            return this;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Datum withUser(User user) {
            this.user = user;
            return this;
        }

        public String getPartimage() {
            return partimage;
        }

        public void setPartimage(String partimage) {
            this.partimage = partimage;
        }

        public Datum withPartimage(String partimage) {
            this.partimage = partimage;
            return this;
        }

        public String getPartname() {
            return partname;
        }

        public void setPartname(String partname) {
            this.partname = partname;
        }

        public Datum withPartname(String partname) {
            this.partname = partname;
            return this;
        }

        public String getPartdescription() {
            return partdescription;
        }

        public void setPartdescription(String partdescription) {
            this.partdescription = partdescription;
        }

        public Datum withPartdescription(String partdescription) {
            this.partdescription = partdescription;
            return this;
        }

        public String getMakemodel() {
            return makemodel;
        }

        public void setMakemodel(String makemodel) {
            this.makemodel = makemodel;
        }

        public Datum withMakemodel(String makemodel) {
            this.makemodel = makemodel;
            return this;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public Datum withCondition(String condition) {
            this.condition = condition;
            return this;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Datum withPrice(String price) {
            this.price = price;
            return this;
        }

        public Boolean getPemissiontocontact() {
            return pemissiontocontact;
        }

        public void setPemissiontocontact(Boolean pemissiontocontact) {
            this.pemissiontocontact = pemissiontocontact;
        }

        public Datum withPemissiontocontact(Boolean pemissiontocontact) {
            this.pemissiontocontact = pemissiontocontact;
            return this;
        }

        public Boolean getSold() {
            return sold;
        }

        public void setSold(Boolean sold) {
            this.sold = sold;
        }

        public Datum withSold(Boolean sold) {
            this.sold = sold;
            return this;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public Datum withUpdated(String updated) {
            this.updated = updated;
            return this;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public Datum withCreated(String created) {
            this.created = created;
            return this;
        }

    }

    public class User {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("last_login")
        @Expose
        private String lastLogin;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("is_admin")
        @Expose
        private Boolean isAdmin;
        @SerializedName("is_active")
        @Expose
        private Boolean isActive;
        @SerializedName("is_superuser")
        @Expose
        private Boolean isSuperuser;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("login_type")
        @Expose
        private String loginType;
        @SerializedName("profileimage")
        @Expose
        private Object profileimage;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("push_notification")
        @Expose
        private Boolean pushNotification;
        @SerializedName("reminder")
        @Expose
        private Boolean reminder;
        @SerializedName("is_staff")
        @Expose
        private Boolean isStaff;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("device_id")
        @Expose
        private String deviceId;
        @SerializedName("groups")
        @Expose
        private List<Object> groups = null;
        @SerializedName("user_permissions")
        @Expose
        private List<Object> userPermissions = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public User withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public User withLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public User withEmail(String email) {
            this.email = email;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public User withName(String name) {
            this.name = name;
            return this;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public User withMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Boolean getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public User withIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public User withIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Boolean getIsSuperuser() {
            return isSuperuser;
        }

        public void setIsSuperuser(Boolean isSuperuser) {
            this.isSuperuser = isSuperuser;
        }

        public User withIsSuperuser(Boolean isSuperuser) {
            this.isSuperuser = isSuperuser;
            return this;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public User withRole(String role) {
            this.role = role;
            return this;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public User withLoginType(String loginType) {
            this.loginType = loginType;
            return this;
        }

        public Object getProfileimage() {
            return profileimage;
        }

        public void setProfileimage(Object profileimage) {
            this.profileimage = profileimage;
        }

        public User withProfileimage(Object profileimage) {
            this.profileimage = profileimage;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public User withPassword(String password) {
            this.password = password;
            return this;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public User withAge(Integer age) {
            this.age = age;
            return this;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public User withLocation(String location) {
            this.location = location;
            return this;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public User withState(String state) {
            this.state = state;
            return this;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public User withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Boolean getPushNotification() {
            return pushNotification;
        }

        public void setPushNotification(Boolean pushNotification) {
            this.pushNotification = pushNotification;
        }

        public User withPushNotification(Boolean pushNotification) {
            this.pushNotification = pushNotification;
            return this;
        }

        public Boolean getReminder() {
            return reminder;
        }

        public void setReminder(Boolean reminder) {
            this.reminder = reminder;
        }

        public User withReminder(Boolean reminder) {
            this.reminder = reminder;
            return this;
        }

        public Boolean getIsStaff() {
            return isStaff;
        }

        public void setIsStaff(Boolean isStaff) {
            this.isStaff = isStaff;
        }

        public User withIsStaff(Boolean isStaff) {
            this.isStaff = isStaff;
            return this;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public User withUpdated(String updated) {
            this.updated = updated;
            return this;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public User withCreated(String created) {
            this.created = created;
            return this;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public User withDeviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public User withDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public List<Object> getGroups() {
            return groups;
        }

        public void setGroups(List<Object> groups) {
            this.groups = groups;
        }

        public User withGroups(List<Object> groups) {
            this.groups = groups;
            return this;
        }

        public List<Object> getUserPermissions() {
            return userPermissions;
        }

        public void setUserPermissions(List<Object> userPermissions) {
            this.userPermissions = userPermissions;
        }

        public User withUserPermissions(List<Object> userPermissions) {
            this.userPermissions = userPermissions;
            return this;
        }

    }

}

