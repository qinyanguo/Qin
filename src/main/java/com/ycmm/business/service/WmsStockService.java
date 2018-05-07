package com.ycmm.business.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.model.Stock;
import net.sf.json.JSONObject;


public interface WmsStockService {

    /**
     * 添加库存
     */
    Stock addStock(Stock stock) throws Exception;

    /**
     * 删除库存
     */
    Stock deleteStock(Stock stock) throws Exception;

    /**
     * Update stock stock.
     * 更新库存
     */
    Stock updateStock(Stock stock) throws Exception;

    /**
     * 查询库存列表
     */
    public ResultBean queryStockList(BizParamBean bizParamBean) throws Exception;

    /**
     * Query stock list list.
     * 查询库存列表（内部调用接口）
     */
    PageInfo<Stock> queryStockList(JSONObject biz_param) throws Exception;

    /**
     * Query stock list stock.
     * 查询库存详情
     */
    Stock queryStockById(Integer id) throws Exception;

    /**
     * Query stock by id result bean.
     */
    public ResultBean queryStockById(BizParamBean bizParamBean) throws Exception;

    public List<Map<String, Object>> queryStockList(Stock stock) throws Exception;

}
