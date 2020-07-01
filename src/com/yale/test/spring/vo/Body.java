package com.yale.test.spring.vo;

/**
 * Body这个类模仿的是Spring_2.5.6.jar包里面的org.springframework.orm.hibernate3.support.HibernateDaoSupport类
 * @author dell
 */
public abstract class Body {
	private BodyTemplate bt;
	/**
	 * Spring_2.5.6.jar整合hibernate就是通过这种方式把SessionFactory注入到HibernateDaoSupport类中的
	 * @param name
	 */
	public void setName(String name) {
		if (this.bt == null || !name.equals(this.bt.getName())) {
			System.out.println("****Body****,注意我被Spring调用了:这里在演示lazy-init懒加载的作用(并且你要注意我这个方法里面根本没有name属性,Spring一样会调用setName方法):" + name);
			this.bt = createBodyTemplate(name);
		}
	}
	
	protected BodyTemplate createBodyTemplate(String name) {
		return new BodyTemplate(name); 
	}
	
	public final String getName(){
		return this.bt != null ? this.bt.getName() : null;
	}
	
	public BodyTemplate getBt() {
		return bt;
	}
	public void setBt(BodyTemplate bt) {
		this.bt = bt;
	}
}
