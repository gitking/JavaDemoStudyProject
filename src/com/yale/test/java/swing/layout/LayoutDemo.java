package com.yale.test.java.swing.layout;

/*
 * 组件(component或称元件)是比我们之前所称的widget更为正确的术语。它们就是你会放在GUI上面的东西。
 * 从技术上来说,widget是个Swing的组件,几乎所有的GUI组件都来自于javax.swing.JComponent.
 * 这些东西是用户会看到并与其交互的。像是Text Field, button, scrollable list, radio button等。
 * 事实上所有的组件都是继承自javax.swing.JComponent。
 * 组件是可以嵌套的
 * 在swing中,几乎所有组件都能够安置其他的组件。也就是说,你可以把任何东西放在其他东西上。但在大部分的情况下,你会把像是按钮或列表等用户交互组件放在
 * 框架和面板等背景组件上。
 * 除了JFrame之外,交互组件与背景组件的差异不太明确。举例来说JPanel通常用在背景上,但是也可以与用户交互。就跟其他组件一样,你也可以向JPanel注册鼠标的点选等事件。
 * 布局管理器(Layout Managers)
 * 布局管理器是个与特定组件相关联的java对象,它大多数是背景组件。布局管理器用来控制所关联组件上携带的其他组件。也就是说,如果某个框架带有面板,而面板带有按钮,则面板的布局
 * 管理器控制着按钮的大小与位置,而框架的布局管理器则控制着面板的大小与位置。按钮因为没有携带其他组件,所以不需要布局管理器。如果面板带有5项组件,
 * 就算这5项都有自己的布局管理器,他们的大小与位置都还是由面板的布局管理器来管理。
 * 下面是嵌套布局的例子:
 * JPanel panelA = new JPanel();
 * JPanel panelB = new JPanel();
 * panelB.add(new JButton("button 1"));
 * panelB.add(new JButton("button 2"));
 * panelB.add(new JButton("button 3"));
 * 面板B的布局管理器控制这3个按钮的大小与位置
 * panelA.add(panelB);面板A控制面板B的大小与位置,但是面板A管不到3个按钮
 * 
 * 有些布局管理器会尊重组件的想法,如果按钮想要30x50像素,布局管理器就会给它这么大的面积。其他的布局管理器可能只会尊重部分的设定。
 * 世界三大收媳妇管理器：border,flow和box
 * BorderLayout:(5个区域,每个区域只有一个组件)
 * 	BorderLayout这个布局管理器会把背景组件分割成5个(上下左右中)区域,每个被管理的区域只能放上一个组件。由此管理员安置的组件通常不会取的
 * 默认的大小。这是框架默认的布局管理器。
 * FlowLayout:(从左到右排列,有必要时会自动换行)
 * 	FlowLayout每个组件都会依照理想的大小呈现,并且会从左到右依照加入的顺序以可能换行的方式排列。
 *  FlowLayout布局会由左至右，由上至下依加入的顺序来安置组件,若宽度超过时就会换行。FlowLayout布局会让组件在长宽上都使用理想的尺寸大小。
 *  因此在组件放不下的时候会被放到下一行。这是面板默认的布局管理器。
 * BoxLayout:(从上到下每行一个)
 * 	BoxLayout它就像FlowLayout一样让每个组件使用默认的大小,并且按照加入的顺序来排列。但BoxLayout是以垂直的方向
 *  来排列(也可以水平,但通常我们只在乎垂直的方式)。不想FlowLayout会自动换行,它让你插入某种类型换行的机制来强制组件从新的一行开始排列。
 *  BoxLayout如同FlowLayout布局一样,它会让组件在长宽上都使用理想的尺寸大小。
 * 框架默认使用BoxLayout布局,面板默认使用FlowLayout布局
 */
public class LayoutDemo {

}
