package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {

    /**
     * 在Matcher类中有matches、lookingAt和find都是匹配目标的方法，但容易混淆，整理它们的区别如下：
     * matches:整个匹配，只有整个字符序列完全匹配成功，才返回True，否则返回False。但如果前部分匹配成功，将移动下次匹配的位置。
     * lookingAt:部分匹配，总是从第一个字符进行匹配,匹配成功了不再继续匹配，匹配失败了,也不继续匹配。
     * find:部分匹配，从当前位置开始匹配，找到一个匹配的子串，将移动下次匹配的位置。
     * reset:给当前的Matcher对象配上个新的目标，目标是就该方法的参数；如果不给参数，reset会把Matcher设到当前字符串的开始处。
     *
     */
    public static void main(String[] args){
        Pattern pattern = Pattern.compile("\\d{3,5}");
//		Pattern pattern = Pattern.compile("123");
        String charSequence = "123435";
        Matcher matcher = pattern.matcher(charSequence);

        //虽然匹配失败，但由于charSequence里面的"123"和pattern是匹配的,所以下次的匹配从位置4开始
        print(matcher.matches());
        //测试匹配位置
        matcher.find();
        //只有掉用了matcher.find()方法后才可以使用matcher.start()方法
        print(matcher.start());

        //使用reset方法重置匹配位置
        matcher.reset();

        //第一次find匹配以及匹配的目标和匹配的起始位置
        print(matcher.find());
        print(matcher.group()+" - "+matcher.start());
        //第二次find匹配以及匹配的目标和匹配的起始位置
        print(matcher.find());
        print(matcher.group()+" - "+matcher.start());

        //第一次lookingAt匹配以及匹配的目标和匹配的起始位置
        print(matcher.lookingAt());
        print(matcher.group()+" - "+matcher.start());

        //第二次lookingAt匹配以及匹配的目标和匹配的起始位置
        print(matcher.lookingAt());
        print(matcher.group()+" - "+matcher.start());
    }

    public static void print(Object o){
        System.out.println(o);
    }
}
