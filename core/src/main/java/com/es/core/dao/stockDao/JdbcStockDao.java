package com.es.core.dao.stockDao;

import com.es.core.dao.SqlQueryConstants;
import com.es.core.dao.phoneDao.rowMapper.StockPropertyRowMapper;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import com.es.core.model.stock.exception.NoSuchStockException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

@Component
public class JdbcStockDao implements StockDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    public void setDataSource(DataSource dataSource){
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Stock> getPhonesStocks(List<Phone> phones) {
        List<Stock> stocks = new ArrayList<>();
        for(Phone phone : phones){
            Stock stock = getStockByPhoneId(phone.getId());
            stock.setPhone(phone);
            stocks.add(stock);
        }
        return stocks;
    }

    @Override
    public void updateStocks(List<Stock> stocks) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(stocks.toArray());
        namedParameterJdbcTemplate.batchUpdate(SqlQueryConstants.StockDao.UPDATE_STOCK_BY_PHONE_ID, batch);
    }

    private Stock getStockByPhoneId(Long phoneId){
        try {
            return jdbcTemplate.queryForObject(SqlQueryConstants.StockDao.SELECT_STOCK_BY_PHONE_ID + phoneId,
                    new StockPropertyRowMapper());

        }
        catch (EmptyResultDataAccessException e){
            throw new NoSuchStockException();
        }
    }
}