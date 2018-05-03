package com.ycmm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import net.sf.json.JSONArray;

@Table(name="wms_stock")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 批次单号 入库单号
     */
    @Column(name = "batch_no")
    private String batchNo;

    /**
     * 药材品名
     */
    @Column(name = "breed_id")
    private Integer breedId;

    @Transient
    private String breedName;
    /**
     * 所属 货主 id编号
     */
    @Column(name = "customer_id")
    private Integer customerId;

    @Transient
    private String customerName,isList, employeeName, societyDepotType;
    @Transient
    private JSONArray imageArray;

    @SuppressWarnings("rawtypes")
    @Transient
    private List ladderPriceList;

    @SuppressWarnings("rawtypes")
    public List getLadderPriceList() {
        return ladderPriceList;
    }

    @SuppressWarnings("rawtypes")
    public void setLadderPriceList(List ladderPriceList) {
        this.ladderPriceList = ladderPriceList;
    }

    public JSONArray getImageArray() {
        return imageArray;
    }

    public void setImageArray(JSONArray imageArray) {
        this.imageArray = imageArray;
    }

    public String getSocietyDepotType() {
        return societyDepotType;
    }

    public void setSocietyDepotType(String societyDepotType) {
        this.societyDepotType = societyDepotType;
    }

    public String getIsList() {
        return isList;
    }

    public void setIsList(String isList) {
        this.isList = isList;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


    /**
     * 所属仓库id编号
     */
    @Column(name = "depot_id")
    private Integer depotId;

    /**
     * 入库明细ID
     */
    @Column(name = "stockInItem_id")
    private Integer stockInItemId;

    /**
     * 原库存id （针对过户处理）
     */
    @Column(name = "stock_id")
    private Integer stockId;

    @Transient
    private String depotName;

    /**
     * 库位id编号
     */
    @Column(name = "site_id")
    private Integer siteId;

    @Transient
    private String siteName;

    /**
     * 库存类型
     */
    @Column(name = "depot_type")
    private String depotType;

    /**
     * 药材品种 形状、规格 属性 json格式 示例： {"key":{"k1":"v1","k2":"v2"}}
     */
    @Column(name = "spec_attribute")
    private String specAttribute;

    /**
     * 可加工  针对社会库存
     */
    @Column(name = "can_process")
    private Integer canProcess;

    /**
     * 可押款  针对社会库存
     */
    @Column(name = "can_deposite")
    private Integer canDeposite;

    /**
     * 过期时间  针对社会库存
     */
    @Column(name = "due_date")
    private Long dueDate;

    /**
     * 联系业务员  针对社会库存
     */
    private Integer employee;

    /**
     * 药材采购价格
     */
    private Double price;

    /**
     * 药材起订量
     */
    @Column(name = "minimum_order_quantity")
    private Double minimumOrderQuantity;

    /**
     * 阶梯价格
     */
    @Column(name = "ladder_price")
    private String ladderPrice;


    /**
     * 品种 产地
     */
    private Integer location;

    @Transient
    private String locationName;

    /**
     * 库存总量 (可用+冻结)
     */
    private Double total;

    /**
     * 库存可用量
     */
    @Column(name = "usable_num")
    private Double usableNum;

    /**
     * 库存冻结量
     */
    @Column(name = "freeze_num")
    private Double freezeNum;

    /**
     * 联系人（货主名）
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * 联系人（货主）手机
     */
    @Column(name = "contact_phone")
    private String contactPhone;


    /**
     * 库存单位 id
     */
    @Column(name = "unit_id")
    private Integer unitId;

    /**
     * 数据状态 0/1 无效/可用
     */
    private Integer status;

    /**
     * 入库存储时间
     */
    @Column(name = "storage_date")
    private Long storageDate;

    /**
     * 库存备注
     */
    private String comment;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    private Long utime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Long ctime;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取批次单号 入库单号
     *
     * @return batch_no - 批次单号 入库单号
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * 设置批次单号 入库单号
     *
     * @param batchNo
     *            批次单号 入库单号
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * 获取药材品名
     *
     * @return breed_id - 药材品名
     */
    public Integer getBreedId() {
        return breedId;
    }

    /**
     * 设置药材品名
     *
     * @param breedId
     *            药材品名
     */
    public void setBreedId(Integer breedId) {
        this.breedId = breedId;
    }

    /**
     * 获取所属 货主 id编号
     *
     * @return customer_id - 所属 货主 id编号
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * 设置所属 货主 id编号
     *
     * @param customerId
     *            所属 货主 id编号
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取所属仓库id编号
     *
     * @return depot_id - 所属仓库id编号
     */
    public Integer getDepotId() {
        return depotId;
    }

    /**
     * 设置所属仓库id编号
     *
     * @param depotId
     *            所属仓库id编号
     */
    public void setDepotId(Integer depotId) {
        this.depotId = depotId;
    }

    /**
     * 获取入库明细ID
     */
    public Integer getStockInItemId() {
        return stockInItemId;
    }

    /**
     * 设置入库明细Id
     */
    public void setStockInItemId(Integer stockInItemId) {
        this.stockInItemId = stockInItemId;
    }

    /**
     * 获取库位id编号
     *
     * @return site_id - 库位id编号
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * 设置库位id编号
     *
     * @param siteId
     *            库位id编号
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * 获取联系人（货主名）
     *
     * @return contact_name - 联系人（货主名）
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置联系人（货主名）
     *
     * @param contactName 联系人（货主名）
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取联系人（货主）手机
     *
     * @return contact_phone - 联系人（货主）手机
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置联系人（货主）手机
     *
     * @param contactPhone 联系人（货主）手机
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }


    /**
     * 获取库存类型
     *
     * @return depot_type - 库存类型
     */
    public String getDepotType() {
        return depotType;
    }

    /**
     * 设置库存类型
     *
     * @param depotType
     *            库存类型
     */
    public void setDepotType(String depotType) {
        this.depotType = depotType;
    }

    /**
     * 获取药材品种 形状、规格 属性 json格式 示例： {"key":{"k1":"v1","k2":"v2"}}
     *
     * @return spec_attribute - 药材品种 形状、规格 属性 json格式 示例： {"key":{"k1":"v1","k2":"v2"}}
     */
    public String getSpecAttribute() {
        return specAttribute;
    }

    /**
     * 设置药材品种 形状、规格 属性 json格式 示例： {"key":{"k1":"v1","k2":"v2"}}
     *
     * @param specAttribute
     *            药材品种 形状、规格 属性 json格式 示例： {"key":{"k1":"v1","k2":"v2"}}
     */
    public void setSpecAttribute(String specAttribute) {
        this.specAttribute = specAttribute;
    }

    /**
     * 获取品种 产地
     *
     * @return location - 品种 产地
     */
    public Integer getLocation() {
        return location;
    }

    /**
     * 设置品种 产地
     *
     * @param location
     *            品种 产地
     */
    public void setLocation(Integer location) {
        this.location = location;
    }

    /**
     * 获取库存总量 (可用+冻结)
     *
     * @return total - 库存总量 (可用+冻结)
     */
    public Double getTotal() {
        return total;
    }

    /**
     * 设置库存总量 (可用+冻结)
     *
     * @param total
     *            库存总量 (可用+冻结)
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getCanProcess() {
        return canProcess;
    }

    public void setCanProcess(Integer canProcess) {
        this.canProcess = canProcess;
    }

    public Integer getCanDeposite() {
        return canDeposite;
    }

    public void setCanDeposite(Integer canDeposite) {
        this.canDeposite = canDeposite;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }

    /**
     * 获取药材采购价格
     *
     * @return price - 药材采购价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置药材采购价格
     *
     * @param price 药材采购价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取药材起订量
     *
     * @return minimum_order_quantity - 药材起订量
     */
    public Double getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    /**
     * 设置药材起订量
     *
     * @param minimumOrderQuantity 药材起订量
     */
    public void setMinimumOrderQuantity(Double minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }

    /**
     * 获取阶梯价格
     *
     * @return ladder_price - 阶梯价格
     */
    public String getLadderPrice() {
        return ladderPrice;
    }

    /**
     * 设置阶梯价格
     *
     * @param ladderPrice 阶梯价格
     */
    public void setLadderPrice(String ladderPrice) {
        this.ladderPrice = ladderPrice;
    }


    /**
     * 获取库存可用量
     *
     * @return usable_num - 库存可用量
     */
    public Double getUsableNum() {
        return usableNum;
    }

    /**
     * 设置库存可用量
     *
     * @param usableNum
     *            库存可用量
     */
    public void setUsableNum(Double usableNum) {
        this.usableNum = usableNum;
    }

    /**
     * 获取库存冻结量
     *
     * @return freeze_num - 库存冻结量
     */
    public Double getFreezeNum() {
        return freezeNum;
    }

    /**
     * 设置库存冻结量
     *
     * @param freezeNum
     *            库存冻结量
     */
    public void setFreezeNum(Double freezeNum) {
        this.freezeNum = freezeNum;
    }

    /**
     * 获取库存单位 id
     *
     * @return unit_id - 库存单位 id
     */
    public Integer getUnitId() {
        return unitId;
    }

    /**
     * 设置库存单位 id
     *
     * @param unitId
     *            库存单位 id
     */
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    /**
     * 获取数据状态 0/1 无效/可用
     *
     * @return status - 数据状态 0/1 无效/可用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置数据状态 0/1 无效/可用
     *
     * @param status
     *            数据状态 0/1 无效/可用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取入库存储时间
     *
     * @return storage_date - 入库存储时间
     */
    public Long getStorageDate() {
        return storageDate;
    }

    /**
     * 设置入库存储时间
     *
     * @param storageDate
     *            入库存储时间
     */
    public void setStorageDate(Long storageDate) {
        this.storageDate = storageDate;
    }

    /**
     * 获取库存备注
     *
     * @return comment - 库存备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置库存备注
     *
     * @param comment
     *            库存备注
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取更新人
     *
     * @return updater - 更新人
     */
    public String getUpdater() {
        return updater;
    }

    /**
     * 设置更新人
     *
     * @param updater
     *            更新人
     */
    public void setUpdater(String updater) {
        this.updater = updater;
    }

    /**
     * 获取更新时间
     *
     * @return utime - 更新时间
     */
    public Long getUtime() {
        return utime;
    }

    /**
     * 设置更新时间
     *
     * @param utime
     *            更新时间
     */
    public void setUtime(Long utime) {
        this.utime = utime;
    }

    /**
     * 获取创建人
     *
     * @return creater - 创建人
     */
    public String getCreater() {
        return creater;
    }

    /**
     * 设置创建人
     *
     * @param creater
     *            创建人
     */
    public void setCreater(String creater) {
        this.creater = creater;
    }

    /**
     * 获取创建时间
     *
     * @return ctime - 创建时间
     */
    public Long getCtime() {
        return ctime;
    }

    /**
     * 设置创建时间
     *
     * @param ctime
     *            创建时间
     */
    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", breedId=").append(breedId);
        sb.append(", stockId=").append(stockId);
        sb.append(", customerId=").append(customerId);
        sb.append(", depotId=").append(depotId);
        sb.append(", siteId=").append(siteId);
        sb.append(", depotType=").append(depotType);
        sb.append(", specAttribute=").append(specAttribute);
        sb.append(", location=").append(location);
        sb.append(", total=").append(total);
        sb.append(", usableNum=").append(usableNum);
        sb.append(", freezeNum=").append(freezeNum);
        sb.append(", unitId=").append(unitId);
        sb.append(", status=").append(status);
        sb.append(", storageDate=").append(storageDate);
        sb.append(", comment=").append(comment);
        sb.append(", updater=").append(updater);
        sb.append(", utime=").append(utime);
        sb.append(", creater=").append(creater);
        sb.append(", ctime=").append(ctime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * 原库存id （针对过户处理）
     *
     * @author Johnson.Jia
     * @date 2017年4月1日 上午11:43:06
     * @return
     */
    public Integer getStockId() {
        return stockId;
    }

    /**
     * 原库存id （针对过户处理）
     *
     * @author Johnson.Jia
     * @date 2017年4月1日 上午11:43:03
     * @param stockId
     */
    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}