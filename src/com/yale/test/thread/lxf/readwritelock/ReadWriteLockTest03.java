package com.yale.test.thread.lxf.readwritelock;

/**
 * 将ReadWriteLock应用于缓存设计
 * ——针对缓慢变化的小数据的缓存实现模型
 * 在JavaEEdev站点的设计中，有几类数据是极少变化的，如ArticleCategory（文档分类），ResourceCategory（资源分类），Board（论坛版面）。在对应的DAO实现中，总是一次性取出所有的数据，例如：
 * List<ArticleCategory> getArticleCategories();
 * 此类数据的特点是：数据量很小，读取非常频繁，变化却极慢（几天甚至几十天才变化一次），如果每次通过DAO从数据库获取数据，则增加了数据库服务器的压力。
 * 为了在不影响整个系统结构的情况下透明地缓存这些数据，可以在Facade一层通过Proxy模式配合ReadWriteLock实现缓存，而客户端和后台的DAO数据访问对象都不受影响：
 * 	现有结构：Web层--> FacadeImpl --> Dao
 *  改进结构:Web层--> FacadeCacheProxy --> FacadeImpl -- Dao
 * 首先，现有的中间层是由Facade接口和一个FacadeImpl具体实现构成的。对ArticleCategory的相关操作在com.yale.test.thread.lxf.readwritelock.facade.impl.FacadeImpl.java中实现如下：
 * 设计代理类FacadeCacheProxy，让其实现缓存com.yale.test.thread.lxf.readwritelock.facade.impl.ArticleCategory的功能：
 * 该代理类的核心是调用读方法getArticleCategories()时，直接从缓存对象FullCache中返回结果，当调用写方法（create，update和delete）时，除了调用target对象的相应方法外，再将缓存对象清空。
 * FullCache便是实现缓存的关键类。为了实现强类型的缓存，采用泛型实现FullCache：
 * AbstractId是所有Domain Object的超类，目的是提供一个String类型的主键，同时便于在Hibernate或其他ORM框架中只需要配置一次JPA注解：
 * FullCache实现以下2个功能：
 * ListgetCachedList()：获取整个缓存的List
 * clearCache()：清除所有缓存
 * 此外，FullCache在缓存失效的情况下，必须从真正的数据源获得数据，因此，抽象方法：
 * protected abstract List<T> doGetList()
 * 负责获取真正的数据。
 * 下面，用ReadWriteLock实现该缓存模型。
 * Java 5平台新增了java.util.concurrent包，该包包含了许多非常有用的多线程应用类，例如ReadWriteLock，这使得开发人员不必自己封装就可以直接使用这些健壮的多线程类。
 * ReadWriteLock是一种常见的多线程设计模式。当多个线程同时访问同一资源时，通常，并行读取是允许的，但是，任一时刻只允许最多一个线程写入，从而保证了读写操作的完整性。下图很好地说明了ReadWriteLock的读写并发模型：
 * https://www.liaoxuefeng.com/article/895888192962464
 */
public class ReadWriteLockTest03 {
	
}
