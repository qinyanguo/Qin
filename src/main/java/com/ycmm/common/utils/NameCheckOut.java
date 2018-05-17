package com.ycmm.common.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 姓名校验处理类
 */
public class NameCheckOut {
	/**
	 * 单姓
	 */
	public final static Set<String> danSurnameSet = new HashSet<String>();

	/**
	 * 复姓
	 */
	public final static Set<String> fuSurnameSet = new HashSet<>();

	/**
	 * 姓名中不可包含的字符
	 */
	public final static Set<String> surnameNot = new HashSet<>();

	static {
		String[] danSurname = { "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许", "何",
				"吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚",
				"范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷",
				"贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾",
				"孟", "平", "黄", "和", "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈",
				"宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江",
				"童", "颜", "郭", "梅", "盛", "林", "刁", "钟", "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "昝",
				"管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应", "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔",
				"吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀", "羊", "於", "惠", "甄", "麴", "家", "封", "芮", "羿", "储", "靳", "汲",
				"邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗", "山", "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋",
				"仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "景", "詹", "束", "龙", "叶", "幸", "司", "韶", "郜",
				"黎", "蓟", "薄", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙", "池", "乔", "阴", "郁", "胥",
				"能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "却", "舄", "璩", "桑", "桂",
				"濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习",
				"宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇",
				"广", "禄", "阙", "东", "殴", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚",
				"那", "简", "饶", "空", "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "後", "荆", "红", "游", "竺", "权", "逯",
				"盖", "益", "桓", "公", "仉", "督", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "归", "海", "岳", "帅", "缑", "亢", "况", "后", "有", "琴",
				"商", "牟", "佘", "佴", "伯", "赏", "墨", "哈", "谯", "笪", "年", "爱", "阳", "佟", "言", "福", "苟", "肖", "代", "泰", "释", "荤", "靖", "绪 ",
				"愈", "硕", "牢", "买", "但", "巧", "枚", "撒", "秘", "亥", "绍", "以", "壬", "森", "斋", "奕", "姒", "朋", "求", "羽", "用", "占", "真", "穰",
				"翦", "闾", "漆", "贵", "贯", "旁", "崇", "栋", "告", "休", "褒", "谏", "锐", "皋", "闳", "在", "歧", "禾", "示", "是", "委", "钊", "频", "嬴",
				"呼", "大", "威", "昂", "律", "仆", "藩", "敬", "令", "鹿" };
		String[] fuSurname = { "百里", "漆雕", "太史", "宗政", "申屠", "东宫", "慕容", "司空", "公良", "皇甫", "子桑", "夏侯", "濮阳", "颛孙", "公仲", "闻人", "宰父", "公山",
				"司徒", "东方", "公羊", "公上", "夹谷", "端木", "公玉", "壤驷", "公西", "乐正", "子车", "子书", "即墨", "公皙", "长孙", "仲孙", "万俟", "吴铭", "东里", "谷梁",
				"梁丘", "微生", "南宫", "褚师", "公冶", "公户", "赫连", "司寇", "上官", "仲长", "闾丘", "公伯", "羊舌", "达奚", "段干", "拓跋", "西门", "贯丘", "澹台", "东郭",
				"轩辕", "公祖", "欧阳", "太叔", "鲜于", "公坚", "令狐", "公乘", "南荣", "公孙", "第五", "巫马", "宇文", "亓官", "钟离", "呼延", "诸葛", "公仪", "独孤", "公门",
				"左丘", "南门", "尉迟", "司马" };

		String[] noSurname = { "先生", "女士", "小姐", "经理", "老总", "硕士", "博士", "医生", "护士", "老师", "律师", "领导", "老板", "秘书", "学生", "书记", "同学", "主席",
				"校长", "大哥" };

		for (String name : danSurname) {
			danSurnameSet.add(name);
		}

		for (String name : fuSurname) {
			fuSurnameSet.add(name);
		}
		for (String name : noSurname) {
			surnameNot.add(name);
		}

	}

	/**
	 * 校验 名字 是否符合 百家姓
	 * @param name
	 * @return
	 */
	public static boolean isCheckSurname(String name) {
		if (StringUtils.isBlank(name) || name.contains(" ") || name.length() > 4 || name.length() < 2) {
			return false;
		}
		boolean result = false;
		if (name.length() > 1) {
			String substring = name.substring(0, 2);
			result = fuSurnameSet.contains(substring);
		}
		if (!result) {
			String str = name.substring(0, 1);
			result = danSurnameSet.contains(str);
		}
		for (String not : surnameNot) {
			if (name.contains(not)) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 校验用户输入名字的长度规范 暂定规则为：15个汉字，30个字符，输入内容暂未做限制 中文+英文/数字不超过15字
	 * 
	 * @param name
	 * @return c >= 0x0391 && c <= 0xFFE5 判定中文标准 c >= 0x0000 && c <= 0x00FF
	 *         判定是否为因为标准
	 */
	public static boolean isCheckName(String name) {
		// String regex = ".*[0-9a-zA-Z]+.*";
		if (StringUtils.isBlank(name)) {
			return false;
		}
		char c = name.charAt(0);
		// Matcher m = Pattern.compile(regex.toString()).matcher(name);
		// if (m.matches() && name.trim().length() > 15) {
		// return false;
		// }
		if (c >= 0x0391 && c <= 0xFFE5) {// 中文判定
			if (name.trim().length() > 15) {
				return false;
			}
		}
		if (c >= 0x0000 && c <= 0x00FF) {// 英文判定
			if (name.trim().length() > 30) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检验名字输入的是否符合标准,是否包含特殊字符
	 * 
	 * @param name
	 * @return m.replaceAll("").trim() 为剔除这些特殊的符号后的标准输入
	 */
	public static boolean isCheckNameNorm(String name) {
		// String s =
		// "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";//拦截表情
		// String ss = "[^a-zA-Z]";//非英文
		// String s = "[^\u4e00-\u9fa5]";//非中文
		String s = "[`~!@#$%∩^_&*+=|{}':;',\\-\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“'。，、？0-9]";// 特殊字符
		// System.out.println(mms.matches());
		String replaceName = name.replaceAll("\\s*", "");
		Pattern p = Pattern.compile(s);
		Matcher m = p.matcher(replaceName);
		int i = replaceName.length() - m.replaceAll("").trim().length();
		 System.out.println(m.replaceAll("").trim().length());
		 System.out.println("****"+replaceName.length());
		if (i > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 检查姓名里面包含的指定特殊汉字
	 * @param name
	 * @return 名字里不能包含【冕冠,药王谷,东壁,药材买卖网】等特殊字符!
	 */
	public static boolean isCheckNameEspecial(String name) {
		if (name.contains("冕冠") || name.contains("药王谷") || name.contains("东壁") || name.contains("药材买卖网") || name.contains("yaocaimaimai")
				|| name.contains("mianguan") || name.contains("药材买卖") || name.contains("东璧") || name.contains("冕冠电子商务")
				|| name.contains("郭冕") || name.contains("于莎莎")) {
			return false;
		}
		return true;
	}

	/**
	 * 输入的文字校验是否有表情
	 * 
	 * @param content
	 *            要检验的内容
	 * @return
	 */
	public static boolean isCheckEmoji(String content) {
		// String s =
		// "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]";//
		// 拦截表情规则
		String s = "[^\\u0020-\\u007E\\u00A0-\\u00BE\\u2E80-\\uA4CF\\uF900-\\uFAFF\\uFE30-\\uFE4F\\uFF00-\\uFFEF\\u0080-\\u009F\\u2000-\\u201f\r\n]";
		Pattern emoji = Pattern.compile(s, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		Matcher emojiMatcher = emoji.matcher(content.toString());
		// System.out.println(emojiMatcher.replaceAll("").trim().length());
		int i = content.length() - emojiMatcher.replaceAll("").trim().length();
		if (i > 0) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		// System.out.println(isCheckEmoji("😢，希望贵公司能把半夏这一味药材也加入进来。谢谢🙏不然注册不进来。不好推荐啊。"));
		if (!isCheckNameEspecial("希望贵公司能把半夏这一味药材也加入进来。我是")) {
			System.out.println(123);
		}
		System.out.println(isCheckNameEspecial("希望贵公司能把半夏这一味药材也加入进来。我是药材买卖网的"));
		if(isCheckNameNorm("你好啊")){
			
		}
		System.out.println(isCheckNameNorm("你好啊 hahha  嗯嗯"));
	}
}
