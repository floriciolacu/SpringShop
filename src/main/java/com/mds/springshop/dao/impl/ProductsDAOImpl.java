package com.mds.springshop.dao.impl;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mds.springshop.dao.ProductsDAO;
import com.mds.springshop.entity.Products;
import com.mds.springshop.model.PaginationResult;
import com.mds.springshop.model.ProductInfo;

@Transactional
@Component
public class ProductsDAOImpl implements ProductsDAO {

	private volatile int categoryType = 5;
	private long priceMin=0;
	private long priceMax=0;
	private int Stock;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public int getStock() {
		return Stock;
	}

	public void setStock(int stock) {
		Stock = stock;
	}
	public long getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(long priceMin) {
		this.priceMin = priceMin;
	}

	public long getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(long priceMax) {
		this.priceMax = priceMax;
	}
    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,//
    		int category,long minPrice,long maxPrice,int stock) {

    	String sql;
    	if (category == 5) {
    		sql = "Select new " + ProductInfo.class.getName() //
                    + "(p.name, p.productsLeftInStock, p.price, p.status, p.categoryId, p.id) " + " from "//
                    + Products.class.getName() + " p "
                    + " where p.status = 1 ";
    		if(minPrice!=0) sql+=" and p.price >= "+minPrice;
    		if(maxPrice!=0) sql+=" and p.price <= "+maxPrice;
    		if(stock!=0) sql+=" and p.productsLeftInStock > 0 ";
    		else sql+=" and p.productsLeftInStock >= 0 ";
    	} else {
    		sql = "Select new " + ProductInfo.class.getName() //
                    + "(p.name, p.productsLeftInStock, p.price, p.status, p.categoryId, p.id) " + " from "//
                    + Products.class.getName() + " p "
                    + " where p.status = 1 and p.categoryId = " + category; 
    		if(minPrice!=0) sql+=" and p.price >= "+minPrice;
    		if(maxPrice!=0) sql+=" and p.price <= "+maxPrice;
    		if(stock!=0) sql+=" and p.productsLeftInStock > 0 ";
    		else sql+=" and p.productsLeftInStock >= 0 ";
    	}
    	
    	Session session = sessionFactory.getCurrentSession();
    	 
    	Query query = session.createQuery(sql);
    	
    	return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
    }
    
    public ProductInfo getProductById(int id) {
    	
    	String sql;
    	
    	sql = "Select new " + ProductInfo.class.getName() //
                + "(p.name, p.productsLeftInStock, p.price, p.status, p.categoryId, p.id) " + " from "//
                + Products.class.getName() + " p " 
                + " where p.id = " + id;
    	
    	Session session = sessionFactory.getCurrentSession();
   	 
    	Query query = session.createQuery(sql);
    	
    	ProductInfo product = (ProductInfo) query.list().get(0);
    	
    	return product;
    }
    
    public ArrayList<ProductInfo> getAllProducts() {

    	String sql;
    		sql = "Select new " + ProductInfo.class.getName() //
                    + "(p.name, p.productsLeftInStock, p.price, p.status, p.categoryId, p.id) " + " from "//
                    + Products.class.getName() + " p "
                    + " where p.status = 1 ";
    	
    	Session session = sessionFactory.getCurrentSession();
    	 
    	Query query = session.createQuery(sql);
    	
    	ArrayList<ProductInfo> product = (ArrayList<ProductInfo>) query.list();
    	
    	return product;
    }
    
    public void updateProduct(ProductInfo productInfo)
	 {
		 int id=productInfo.getId();
		 Products product=null;
		 
//		 product=this.findProductById(id);
		
		 product.setName(productInfo.getName());
//		 product.setDescription(productInfo.getDescription);
		 product.setPrice(productInfo.getPrice());
	 }

}