package com.yale.test.thread.lxf.readwritelock.facade.impl;

public class FacadeCacheProxy implements Facade {
	private Facade target;
	
	public void setFacadeTarget(Facade target) {
		this.target = target;
	}
	
	//定义缓存对象:
	private FullCache<ArticleCategory> cache = new FullCache<ArticleCategory>() {
		//how to  get real data when cache is unavailable:
		protected List<ArticleCategory> doGetList() {
			return target.queryArticleCategories();
		}
	};
	
	//从缓存返回数据:
	public List<ArticleCategory> queryArticleCategories() {
		return cache.getCachedList();
	}
	
	//创建新的ArticleCategory后,让缓存失效:
	public void createArticleCategory(ArticleCategory category) {
		target.createArticleCategory(category);
		cache.clearCache();
	}
	
	//删除某个ArticleCategory后，让缓存失效：
	public void deleteArticleCategory(ArticleCategory category) {
		target.deleteArticleCategory(category);
		cache.clearCache();
	}
}
