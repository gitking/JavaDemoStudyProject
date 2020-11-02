package com.yale.test.design.interpreter.observer;

/*
 * 观察者
 * 定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
 * 观察者模式（Observer）又称发布-订阅模式（Publish-Subscribe：Pub/Sub）。它是一种通知机制，让发送通知的一方（被观察方）和接收通知的一方（观察者）能彼此分离，互不影响。
 * 要理解观察者模式，我们还是看例子。
 * 假设一个电商网站，有多种Product（商品），同时，Customer（消费者）和Admin（管理员）对商品上架、价格改变都感兴趣，希望能第一时间获得通知。
 * 于是，Store（商场）可以这么写：
 * public class Store {
	    Customer customer;
	    Admin admin;
	
	    private Map<String, Product> products = new HashMap<>();
	
	    public void addNewProduct(String name, double price) {
	        Product p = new Product(name, price);
	        products.put(p.getName(), p);
	        // 通知用户:
	        customer.onPublished(p);
	        // 通知管理员:
	        admin.onPublished(p);
	    }
	
	    public void setProductPrice(String name, double price) {
	        Product p = products.get(name);
	        p.setPrice(price);
	        // 通知用户:
	        customer.onPriceChanged(p);
	        // 通知管理员:
	        admin.onPriceChanged(p);
	    }
	}
 * 我们观察上述Store类的问题：它直接引用了Customer和Admin。先不考虑多个Customer或多个Admin的问题，上述Store类最大的问题是，
 * 如果要加一个新的观察者类型，例如工商局管理员，Store类就必须继续改动。
 * 因此，上述问题的本质是Store希望发送通知给那些关心Product的对象，但Store并不想知道这些人是谁。观察者模式就是要分离被观察者和观察者之间的耦合关系。
 * 要实现这一目标也很简单，Store不能直接引用Customer和Admin，相反，它引用一个ProductObserver接口，任何人想要观察Store，只要实现该接口，并且把自己注册到Store即可：
 * 就是这么一个小小的改动，使得观察者类型就可以无限扩充，而且，观察者的定义可以放到客户端：
 * 甚至可以注册匿名观察者：
 * 用一张图画出观察者模式：
 *  ┌─────────┐      ┌───────────────┐
	│  Store  │─ ─ ─>│ProductObserver│
	└─────────┘      └───────────────┘
	     │                   ▲
	                         │
	     │             ┌─────┴─────┐
	     ▼             │           │
	┌─────────┐   ┌─────────┐ ┌─────────┐
	│ Product │   │  Admin  │ │Customer │ ...
	└─────────┘   └─────────┘ └─────────┘
 * 观察者模式也有很多变体形式。有的观察者模式把被观察者也抽象出接口：
 * 对应的实体被观察者就要实现该接口：
 * 有些观察者模式把通知变成一个Event对象，从而不再有多种方法通知，而是统一成一种：
 * 让观察者自己从Event对象中读取通知类型和通知数据。
 * 广义的观察者模式包括所有消息系统。所谓消息系统，就是把观察者和被观察者完全分离，通过消息系统本身来通知：
 *                   ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
	                   Messaging System
	                 │                       │
	                   ┌──────────────────┐
	              ┌──┼>│Topic:newProduct  │──┼─┐    ┌─────────┐
	              │    └──────────────────┘    ├───>│ConsumerA│
	┌─────────┐   │  │ ┌──────────────────┐  │ │    └─────────┘
	│Producer │───┼───>│Topic:priceChanged│────┘
	└─────────┘   │  │ └──────────────────┘  │
	              │    ┌──────────────────┐         ┌─────────┐
	              └──┼>│Topic:soldOut     │──┼─────>│ConsumerB│
	                   └──────────────────┘         └─────────┘
	                 └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * 消息发送方称为Producer，消息接收方称为Consumer，Producer发送消息的时候，必须选择发送到哪个Topic。Consumer可以订阅自己感兴趣的Topic，
 * 从而只获得特定类型的消息。
 * 使用消息系统实现观察者模式时，Producer和Consumer甚至经常不在同一台机器上，并且双方对对方完全一无所知，因为注册观察者这个动作本身都在消息系统中完成，而不是在Producer内部完成。
 * 此外，注意到我们在编写观察者模式的时候，通知Observer是依靠语句：
 * 这说明各个观察者是依次获得的同步通知，如果上一个观察者处理太慢，会导致下一个观察者不能及时获得通知。此外，如果观察者在处理通知的时候，发生了异常，还需要被观察者处理异常，才能保证继续通知下一个观察者。
 * 思考：如何改成异步通知，使得所有观察者可以并发同时处理？
 * 有的童鞋可能发现Java标准库有个java.util.Observable类和一个Observer接口，用来帮助我们实现观察者模式。但是，这个类非常不！好！用！实现观察者模式的时候，也不推荐借助这两个东东。
 * 练习
 * 给Store增加一种类型的观察者，并把通知改为异步。
 * 小结
 * 观察者模式，又称发布-订阅模式，是一种一对多的通知机制，使得双方无需关心对方，只关心通知本身。
 */
public class Test {
	public static void main(String[] args) {
		//observer:
		Admin a = new Admin();
		Customer c = new Customer();
		
		//Store
		Store store = new Store();
		
		//注册观察者
		store.addObserver(a);
		store.addObserver(c);
		//注册匿名观察者
		store.addObserver(new ProductObserver(){
			@Override
			public void onPublished(Product product) {
				System.out.println("[Log] on product published: " + product);
			}
			
			@Override
			public void onPriceChanged(Product product) {
				System.out.println("[Log] on product price changed: " + product);
			}
		});
		
		//operation:
		store.addNewProduct("Design Patterns", 35.6);
		store.addNewProduct("Effevtive Java", 50.8);
		store.addNewProduct("Design Patterns", 31.9);
	}
}

class Customer implements ProductObserver {
	@Override
	public void onPublished(Product product) {
		System.out.println("[Customer] on product published: " + product);
	}

	@Override
	public void onPriceChanged(Product product) {
		System.out.println("[Customer] on product changed: " + product);
	}
}

class Admin implements ProductObserver {
	
	@Override
	public void onPublished(Product product) {
		System.out.println("[Admin] on product published: " + product);
	}
	
	@Override
	public void onPriceChanged(Product product) {
		System.out.println("[Admin] on product price changed: " + product);
	}
}
