package com.developers.vmrapp.models;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.List;

@Generated("jsonschema2pojo")
public class UderEditModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public UderEditModel withData(Data data) {
        this.data = data;
        return this;
    }

    @Generated("jsonschema2pojo")
    public class Data {

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
        private String age;
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

        public Data withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public Data withLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Data withEmail(String email) {
            this.email = email;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Data withName(String name) {
            this.name = name;
            return this;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Data withMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Boolean getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Data withIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Data withIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Boolean getIsSuperuser() {
            return isSuperuser;
        }

        public void setIsSuperuser(Boolean isSuperuser) {
            this.isSuperuser = isSuperuser;
        }

        public Data withIsSuperuser(Boolean isSuperuser) {
            this.isSuperuser = isSuperuser;
            return this;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Data withRole(String role) {
            this.role = role;
            return this;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public Data withLoginType(String loginType) {
            this.loginType = loginType;
            return this;
        }

        public Object getProfileimage() {
            return profileimage;
        }

        public void setProfileimage(Object profileimage) {
            this.profileimage = profileimage;
        }

        public Data withProfileimage(Object profileimage) {
            this.profileimage = profileimage;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Data withPassword(String password) {
            this.password = password;
            return this;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public Data withAge(String age) {
            this.age = age;
            return this;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Data withLocation(String location) {
            this.location = location;
            return this;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Data withState(String state) {
            this.state = state;
            return this;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Data withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Boolean getPushNotification() {
            return pushNotification;
        }

        public void setPushNotification(Boolean pushNotification) {
            this.pushNotification = pushNotification;
        }

        public Data withPushNotification(Boolean pushNotification) {
            this.pushNotification = pushNotification;
            return this;
        }

        public Boolean getReminder() {
            return reminder;
        }

        public void setReminder(Boolean reminder) {
            this.reminder = reminder;
        }

        public Data withReminder(Boolean reminder) {
            this.reminder = reminder;
            return this;
        }

        public Boolean getIsStaff() {
            return isStaff;
        }

        public void setIsStaff(Boolean isStaff) {
            this.isStaff = isStaff;
        }

        public Data withIsStaff(Boolean isStaff) {
            this.isStaff = isStaff;
            return this;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public Data withUpdated(String updated) {
            this.updated = updated;
            return this;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public Data withCreated(String created) {
            this.created = created;
            return this;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public Data withDeviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Data withDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public List<Object> getGroups() {
            return groups;
        }

        public void setGroups(List<Object> groups) {
            this.groups = groups;
        }

        public Data withGroups(List<Object> groups) {
            this.groups = groups;
            return this;
        }

        public List<Object> getUserPermissions() {
            return userPermissions;
        }

        public void setUserPermissions(List<Object> userPermissions) {
            this.userPermissions = userPermissions;
        }

        public Data withUserPermissions(List<Object> userPermissions) {
            this.userPermissions = userPermissions;
            return this;
        }

    }

}