package com.ycmm.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageInfo;
import com.ycmm.base.BizParamBean;
import com.ycmm.base.ResultBean;
import com.ycmm.business.mapper.StockMapper;
import com.ycmm.business.service.WmsStockService;
import com.ycmm.model.Stock;
import com.ycmm.utils.IdKit;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.pagehelper.page.PageMethod.startPage;

@SuppressWarnings("all")
@Service
public class WmsStockServiceImpl implements WmsStockService {

	@Autowired
//    @Resource
	private StockMapper stockMapper;


	@Override
	public Stock addStock(Stock stock) throws Exception {
		stock.setBatchNo(IdKit.getNumberId());
		stock.setCtime(System.currentTimeMillis());
		stock.setUtime(stock.getCtime());
		stock.setStorageDate(stock.getCtime());
		Double usableNum = stock.getUsableNum() == null ? 0d : stock.getUsableNum();
		Double freezeNum = stock.getFreezeNum() == null ? 0d : stock.getFreezeNum();
		stock.setTotal(usableNum + freezeNum);
		stockMapper.insertSelective(stock);
		return stock;
	}

	@Override
	public Stock deleteStock(Stock stock) throws Exception {
		// 数据效验
		stock.setStatus(0);
//		stockMapper.updateByPrimaryKeySelective(stock);
		return stock;
	}

	@Override
	public Stock updateStock(Stock stock) throws Exception {
		// 数据效验
		stock.setStatus(null);
		stockMapper.updateByPrimaryKeySelective(stock);
		return stock;
	}

	@Override
	public ResultBean queryStockList(BizParamBean bizParamBean) throws Exception {
		JSONObject biz_param = bizParamBean.getBiz_param();
		PageInfo<Stock> stockPageInfo = queryStockList(biz_param);
		return new ResultBean(stockPageInfo);
	}

	@SuppressWarnings("static-access")
	@Override
	public PageInfo<Stock> queryStockList(JSONObject biz_param) throws Exception {
		Stock stock = (Stock) JSONObject.toBean(biz_param, Stock.class);
		long beginTime = biz_param.optLong("beginTime", -1);
		long endTime = biz_param.optLong("endTime", -1);
		int page = biz_param.optInt("page", 0);
		int pageSize = biz_param.optInt("pageSize", 20);
		String isList = biz_param.optString("isList", null);
		Example example = new Example(Stock.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("breedId", stock.getBreedId());
		criteria.andLike("batchNo", stock.getBatchNo());
		criteria.andEqualTo("location", stock.getLocation());
		criteria.andEqualTo("depotId", stock.getDepotId());
		criteria.andEqualTo("siteId", stock.getSiteId());
		criteria.andEqualTo("customerId", stock.getCustomerId());
		criteria.andLike("contactName", stock.getContactName());
		criteria.andLike("contactPhone", stock.getContactPhone());
		criteria.andEqualTo("status", 1);
		if (isList == null) {
			criteria.andGreaterThan("total", 0);
		}
		if (beginTime != -1) {
			criteria.andGreaterThanOrEqualTo("storageDate", beginTime);
		}
		example.setOrderByClause(" ctime DESC ");
		startPage(page, pageSize);
		List<Stock> list = stockMapper.selectByExample(example);

		return new PageInfo<>(list);
	}

	@Override
	public Stock queryStockById(Integer id) throws Exception {
		Stock stock = stockMapper.selectByPrimaryKey(id);
		if (stock != null && stock.getSpecAttribute() != null) {
			stock.setBreedName(JSONObject.fromObject(stock.getSpecAttribute()).keys().next().toString());
		}
		return stock;
	}

	@Override
	public ResultBean queryStockById(BizParamBean bizParamBean) throws Exception {
		return new ResultBean(queryStockById(bizParamBean.getBiz_param().optInt("id")));
	}

    @SuppressWarnings("static-access")
    @Override
    public List<Map<String, Object>> queryStockList(Stock stock) throws Exception {
            Example example = new Example(Stock.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("breedId", stock.getBreedId());
            criteria.andLike("batchNo", stock.getBatchNo());
            criteria.andEqualTo("location", stock.getLocation());
            criteria.andEqualTo("depotId", stock.getDepotId());
            criteria.andEqualTo("siteId", stock.getSiteId());
//        criteria.andEqualTo("customerId", stock.getCustomerId());
            criteria.andEqualTo("customerId", 352233);
            criteria.andLike("contactName", stock.getContactName());
            criteria.andLike("contactPhone", stock.getContactPhone());
            criteria.andEqualTo("status", 1);
            criteria.andNotEqualTo("depotType", stock.getSocietyDepotType());
            List<Stock> stocks = stockMapper.selectByExample(example);
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < stocks.size(); i++) {
                Stock stock1 =  stocks.get(i);
                String string = JSON.toJSONString(stock1, SerializerFeature.WriteDateUseDateFormat);
                Map<String, Object> map = JSON.parseObject(string, Map.class);
                list.add(map);
            }
            return list;
    }

}
