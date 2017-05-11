package com.xz.activeplan.entity;

import java.io.Serializable;
import java.util.List;

public class TeacherBean implements Serializable {
	private int tea_id;
	private String realname;					//姓名
	private String position;					//头衔职位
	private String company;					//任职机构
	private String worktime;					//工作年限
	private String phone;						//手机号码
	private String email;						//邮箱地址
	private String address;						//常驻地址
	private String certifier;						//身份证明人
	private String card;							//名片
	private String skill;							//技能
	private String intro;							//介绍
	private String link;							//相关链接
	private String cover;						//个人相片
	private String favicon;						//个人头像
	private List<GalleryBean> gallery; 	//图册
	private String caption;						//话题标题
	private String gambit;						//话题内容
	private String duration;					//见面时长
	private double price;						//每小时价格
	private String category;					//类别代码
	private int pushid;							//推荐类别 0默认 1推荐
	private int authstatus;						//认证状态 1待验 2通过 3未过
	private int usr_id;							//用户ID
	private boolean isattend;				//是否被关注
	private int follownum;						//关注人数
	private String token;
    private String pushurl;




	public TeacherBean() {
	}

	public TeacherBean(int tea_id, String realname, String position, String company,
					   String worktime, String phone, String email, String address,
					   String certifier, String card, String skill, String intro, String link,
					   String cover, String favicon, List<GalleryBean> gallery, String caption,
					   String gambit, String duration, double price, String category,
					   int pushid, int authstatus, int usr_id, boolean isattend, int follownum,
					   String token, String pushurl) {
		this.tea_id = tea_id;
		this.realname = realname;
		this.position = position;
		this.company = company;
		this.worktime = worktime;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.certifier = certifier;
		this.card = card;
		this.skill = skill;
		this.intro = intro;
		this.link = link;
		this.cover = cover;
		this.favicon = favicon;
		this.gallery = gallery;
		this.caption = caption;
		this.gambit = gambit;
		this.duration = duration;
		this.price = price;
		this.category = category;
		this.pushid = pushid;
		this.authstatus = authstatus;
		this.usr_id = usr_id;
		this.isattend = isattend;
		this.follownum = follownum;
		this.token = token;
		this.pushurl =pushurl;
	}

	public String getPushurl() {
		return pushurl;
	}

	public void setPushurl(String pushurl) {
		this.pushurl = pushurl;
	}

	public int getTea_id() {
		return tea_id;
	}

	public void setTea_id(int tea_id) {
		this.tea_id = tea_id;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCertifier() {
		return certifier;
	}

	public void setCertifier(String certifier) {
		this.certifier = certifier;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}

	public List<GalleryBean> getGallery() {
		return gallery;
	}

	public void setGallery(List<GalleryBean> gallery) {
		this.gallery = gallery;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getGambit() {
		return gambit;
	}

	public void setGambit(String gambit) {
		this.gambit = gambit;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getPushid() {
		return pushid;
	}

	public void setPushid(int pushid) {
		this.pushid = pushid;
	}

	public int getAuthstatus() {
		return authstatus;
	}

	public void setAuthstatus(int authstatus) {
		this.authstatus = authstatus;
	}

	public int getUsr_id() {
		return usr_id;
	}

	public void setUsr_id(int usr_id) {
		this.usr_id = usr_id;
	}

	public boolean isattend() {
		return isattend;
	}

	public void setIsattend(boolean isattend) {
		this.isattend = isattend;
	}

	public int getFollownum() {
		return follownum;
	}

	public void setFollownum(int follownum) {
		this.follownum = follownum;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "TeacherBean{" +
				"tea_id=" + tea_id +
				", realname='" + realname + '\'' +
				", position='" + position + '\'' +
				", company='" + company + '\'' +
				", worktime='" + worktime + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", address='" + address + '\'' +
				", certifier='" + certifier + '\'' +
				", card='" + card + '\'' +
				", skill='" + skill + '\'' +
				", intro='" + intro + '\'' +
				", link='" + link + '\'' +
				", cover='" + cover + '\'' +
				", favicon='" + favicon + '\'' +
				", gallery=" + gallery +
				", caption='" + caption + '\'' +
				", gambit='" + gambit + '\'' +
				", duration='" + duration + '\'' +
				", price=" + price +
				", category='" + category + '\'' +
				", pushid=" + pushid +
				", authstatus=" + authstatus +
				", usr_id=" + usr_id +
				", isattend=" + isattend +
				", follownum=" + follownum +
				", token='" + token + '\'' +
				", pushurl='" + pushurl + '\'' +
				'}';
	}

	public  class GalleryBean implements Serializable{
			private int gal_id;
			private String imgurl;
			private int tea_id;

			public int getGal_id() {
				return gal_id;
			}

			public void setGal_id(int gal_id) {
				this.gal_id = gal_id;
			}

			public String getImgurl() {
				return imgurl;
			}

			public void setImgurl(String imgurl) {
				this.imgurl = imgurl;
			}

			public int getTea_id() {
				return tea_id;
			}

			public void setTea_id(int tea_id) {
				this.tea_id = tea_id;
			}
		}
}