package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

//：搜索接口
public class BannerExpand implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;


    private Integer id;//":1, // 横幅ID
    private String photoUrl;//":"https://test-chain.oss-cn-beijing.aliyuncs.com/exchang_0919171214801661.jpg",// 横幅图片
    private Integer type;//":0, // 跳转类型 0不跳转 1跳转H5页面 2app原生页面  //类型码值0衬衫1套西
    private String androidUrl;//":"0",//安卓跳转路径
    private String iosUrl;//":"0",//IOS跳转路径
    private Integer status;//":1,// 状态 0待上架1展示中
    private Integer pushAddr;//":1,// 上架位置 0 首页 1 社区
    private Integer sort;//":1,// 排序字段
    private String description;//":"阿斯蒂芬",// 描述
    private String pushTime;//":"2018-09-19 17:40:19",// 上架时间
    private String createTime;//":"2018-09-19 17:12:15"// 创建时间
    private String describeName;//":"新人注册奖励",//记录描述
    private Integer changeType;//";:1, //变化类型1加0减
    private String changeName;//":"收入-红包",//变化目录
    private String changeNumber;//":299, //变化值
    private String  rechargeAmount;//":1000, //充值金额
    private String  backAmount;//":50, //返现金额
    private Integer level;//":1//用户等级 1门主 2 香主 3堂主 4阁主
    private Integer supplierId;//":6, //供应商ID
    private String name;//":"百位CEO活动套西",//面料名称
    private Integer stockCount;//":9999, //库存数量
    private String redPacketLimit;//":9999, //可使用红包
    private String showPrice;//":9999, //面料销售价
    private String orderNo;//":"order20181118154417_7",//订单号
    private String appointNo;//":"appoint20181115171850_7",//预约号
    private Integer userId;//":7, //用户ID
    private Integer measureId;//":2, //量体师ID
    private String sex;//":1, //用户性别1男0女
    private Integer isUpload;//":0, //是否上传量体数据1是0否
    private String phone;//":"15207101294",//收货手机号
    private String province;//":"湖北省",//收货省份
    private String city;//":"武汉市",//收货城市
    private String area;//":"洪山区",//收货地区
    private String address;//":"保利华都",//收货详细地址
    private String payPrice;//":9867, //实际支付金额
    private String payTime;//":1542527601000, //支付时间
    private Integer expressStatus;//":0, //0定制中 1 已发货 2已收货
    private String expressName;//":null, //快递名称
    private String expressNo;//":null, //快递单号
    private String appointTime;//":1542458250000, //预约时间
    private String measureName;//":"王晓",//量体师姓名
    private String measurePhone;//量体师电话
    private String activityName;//"百位CEO活动", //活动名称
    private String giftCardName;//":"轻奢衬衫礼品卡",
    private String giftListPhoto;//":"https://dg.dingzhilian.com/upload_dz/banner/181211170253478823.jpg",
    private String costPrice;//":195,
    private String totalNumber;//":null,
    private String goodsId;//", "
     private String price;//", "
     private String totalPrice;//"
    private String sceneName;//":"送父亲场景",
    private String sceneMainPhoto;//":"https://dg.dingzhilian.com/upload_dz/banner/181211170702053576.jpg"
    private String productName;//":"纯棉时尚商务休闲衬衫",//商品名称
    private String mainPhoto;//":"https://dg.dingzhilian.com/upload_dz/banner/181124174217228460.jpg",//商品主图
    private String fabricName;//":"意大利进口奢牌康可利尼（顶级面料）",//商品面料
    private String discounPrice;//":949, //折扣价格
    private String redPacketRatio;//":50, //红包可使用比例
    private String deductionRedPacket;//":484, //展示价减去红包价格
    private String publicTime;//":null, //上架时间
    private Integer isImported;//":1, //是否进口 0否 1是
    private Integer isFine;//":0, //是否精品 0否 1是
    private Integer isHot;//":1//是否热门 0否 1是
    private String amount;//":33,
    private String   measureRemark;//
    private String    giftCardId;//
    private String  styleFrontPhoto;//":"https://dg.dingzhilian.com/upload_dz/banner/181214090705505680.jpg",//样式图片
    private String  freight;//":25//运费
    private String   useCount;//
    private String  isCanUser;//":0, //是否可使用0否1是
    private String  giftCardCount;//":1//礼品卡数量

    public String getIsCanUser() {
        return isCanUser;
    }

    public void setIsCanUser(String isCanUser) {
        this.isCanUser = isCanUser;
    }

    public String getGiftCardCount() {
        return giftCardCount;
    }

    public void setGiftCardCount(String giftCardCount) {
        this.giftCardCount = giftCardCount;
    }

    public String getUseCount() {
        return useCount;
    }

    public void setUseCount(String useCount) {
        this.useCount = useCount;
    }

    public String getStyleFrontPhoto() {
        return styleFrontPhoto;
    }

    public void setStyleFrontPhoto(String styleFrontPhoto) {
        this.styleFrontPhoto = styleFrontPhoto;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getGiftCardId() {
        return giftCardId;
    }

    public void setGiftCardId(String giftCardId) {
        this.giftCardId = giftCardId;
    }

    public String getMeasureRemark() {
        return measureRemark;
    }

    public void setMeasureRemark(String measureRemark) {
        this.measureRemark = measureRemark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }

    public String getDiscounPrice() {
        return discounPrice;
    }

    public void setDiscounPrice(String discounPrice) {
        this.discounPrice = discounPrice;
    }

    public String getRedPacketRatio() {
        return redPacketRatio;
    }

    public void setRedPacketRatio(String redPacketRatio) {
        this.redPacketRatio = redPacketRatio;
    }

    public String getDeductionRedPacket() {
        return deductionRedPacket;
    }

    public void setDeductionRedPacket(String deductionRedPacket) {
        this.deductionRedPacket = deductionRedPacket;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public Integer getIsImported() {
        return isImported;
    }

    public void setIsImported(Integer isImported) {
        this.isImported = isImported;
    }

    public Integer getIsFine() {
        return isFine;
    }

    public void setIsFine(Integer isFine) {
        this.isFine = isFine;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneMainPhoto() {
        return sceneMainPhoto;
    }

    public void setSceneMainPhoto(String sceneMainPhoto) {
        this.sceneMainPhoto = sceneMainPhoto;
    }



    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getGiftCardName() {
        return giftCardName;
    }

    public void setGiftCardName(String giftCardName) {
        this.giftCardName = giftCardName;
    }

    public String getGiftListPhoto() {
        return giftListPhoto;
    }

    public void setGiftListPhoto(String giftListPhoto) {
        this.giftListPhoto = giftListPhoto;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMeasurePhone() {
        return measurePhone;
    }

    public void setMeasurePhone(String measurePhone) {
        this.measurePhone = measurePhone;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAppointNo() {
        return appointNo;
    }

    public void setAppointNo(String appointNo) {
        this.appointNo = appointNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMeasureId() {
        return measureId;
    }

    public void setMeasureId(Integer measureId) {
        this.measureId = measureId;
    }


    public Integer getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(Integer isUpload) {
        this.isUpload = isUpload;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Integer getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(Integer expressStatus) {
        this.expressStatus = expressStatus;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }



    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public String getRedPacketLimit() {
        return redPacketLimit;
    }

    public void setRedPacketLimit(String redPacketLimit) {
        this.redPacketLimit = redPacketLimit;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }


    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescribeName() {
        return describeName;
    }

    public void setDescribeName(String describeName) {
        this.describeName = describeName;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public String getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(String changeNumber) {
        this.changeNumber = changeNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPushAddr() {
        return pushAddr;
    }

    public void setPushAddr(Integer pushAddr) {
        this.pushAddr = pushAddr;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
