package com.yale.test.thread.lxf.readwritelock.facade.impl;

public class FacadeImpl implements Facade {
	
	protected CategoryDao categoryDao;
	
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}
	
	//读操作
	public ArticleCategory queryArticleCategory(Serializable id) {
		return categoryDao.queryArticleCategory(id);
	}
	
	//读操作
	public List<AtricleCategory> queryArticleCategories() {
		return categoryDao.queryArticleCategories();
	}
	
	//写操作
	public void createArticleCatetgory(ArticleCategory category) {
		categoryDao.delete(category);
	}
	
	//写操作
	public void updateArticleCategory(ArticleCategory category) {
		categoryDao.update(category);
	}
}
