package com.ycmm.system;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Inherited作用是，使用此注解声明出来的自定义注解，在使用此自定义注解时，如果注解在类上面时，
 * 子类会自动继承此注解，否则的话，子类不会继承此注解。这里一定要记住，使用Inherited声明出来的注解，
 * 只有在类上使用时才会有效，对方法，属性等其他无效
 * 
 * @Target说明了Annotation所修饰的对象范围：Annotation可被用于 packages、types（类、接口、枚举、Annotation类型）、
 * 类型成员（方法、构造方法、成员变量、枚举值）、方法参数和本地变量（如循环变量、catch参数）。在Annotation类型的声明中使用了target可更加明晰其修饰的目标。
 * 作用：用于描述注解的使用范围（即：被描述的注解可以用在什么地方）
 * 取值(ElementType)有：
 * 1.CONSTRUCTOR:用于描述构造器
 * 2.FIELD:用于描述域
 * 3.LOCAL_VARIABLE:用于描述局部变量
 * 4.METHOD:用于描述方法
 * 5.PACKAGE:用于描述包
 * 6.PARAMETER:用于描述参数
 * 7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
 *
 * @Retention定义了该Annotation被保留的时间长短：某些Annotation仅出现在源代码中，而被编译器丢弃；
 * 而另一些却被编译在class文件中；编译在class文件中的Annotation可能会被虚拟机忽略，而另一些在class被装载时
 * 将被读取（请注意并不影响class的执行，因为Annotation与class在使用上是被分离的）。使用这个meta-Annotation可以对 Annotation的“生命周期”限制。
 *　取值（RetentionPoicy）有：
 * 1.SOURCE:在源文件中有效（即源文件保留）
 * 2.CLASS:在class文件中有效（即class保留）
 * 3.RUNTIME:在运行时有效（即运行时保留）
 * 
 * @Documented用于描述其它类型的annotation应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化。Documented是一个标记注解，没有成员。
 * 
 * 
 * 注解元素的默认值：
 * 注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，非基本类型的注解元素的值不可为null。因此, 使用空字符串或0作为默认值是一种常用的做法。
 * 这个约束使得处理器很难表现一个元素的存在或缺失的状态，因为每个注解的声明中，所有元素都存在，并且都具有相应的值，为了绕开这个约束，我们只能定义一些特殊的值，
 * 例如空字符串或者负数，一次表示某个元素不存在，在定义注解时，这已经成为一个习惯用法。
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoveLoginCheck {

}










