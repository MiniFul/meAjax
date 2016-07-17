package ajax.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ajax.model.ItemStatus;
import ajax.model.JokeType;
import ajax.tools.HibernateUtil;
import ajax.tools.Tools;

public class TypePage extends Entity<TypePage>{
	public static final String MAX_PAGE_PREFIX = "aj-type-page-maxpage-of-joketype-id-";
	
	
	private int id;
	private String items;
	private int type;
	private int page;
	private String dateEntered;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getDateEntered() {
		return dateEntered;
	}
	public void setDateEntered(String dateEntered) {
		this.dateEntered = dateEntered;
	}
	
	/**
	 * 根据jokeType 类型生成对应新的一页
	 * @param jokeType
	 * @return 
	 */
	public static boolean generateOnePageOf(JokeType jokeType) {
		int maxPage = getMaxPageOf(jokeType);
		List<Item> items = Item.getItemsOfSpecifiedJokeTypeAndIsNotInTypePage(jokeType, 20);
		
		
		if (items.size() < 20) {
			System.out.println("JokeTYpe =" + jokeType.getId() + ", 不足20个item");
			return false;
		}
		List<Integer> idList = new ArrayList<Integer>();
		
		for(Item item : items) {
			idList.add(item.getId());
		}
		
		// 1. 保存 tp对象
		TypePage tp = new TypePage();
		tp.setItems(Tools.join(idList, ","));
		tp.setPage(maxPage + 1);
		tp.setType(jokeType.getId());
		
		if (tp.save()) {
			// 2. 修改jokeType最大页码
			TypePage.setMaxPageOf(jokeType, maxPage + 1);
			
			for(Item item : items) {
				// 3.依次为item添加状态
				item.addItemStatus(ItemStatus.IS_SAVE_TO_TYPE_PAGE);
				item.update();
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	public static int getMaxPageOf(JokeType jokeType) {
		String maxPage = Tools.getConfig(MAX_PAGE_PREFIX + jokeType.getId());
		
		if (maxPage == null || maxPage.equals("")) {
			return 0;
		} else {
			return Integer.parseInt(maxPage);
		}
	}
	
	public static void setMaxPageOf(JokeType jokeType, int maxPage) {
		Tools.setConfig(MAX_PAGE_PREFIX + jokeType.getId(), maxPage + "");
	}
	public static List<Item> getItemsByPageAndType(int page, int type) {
		Session session = HibernateUtil.getSession();
		
		int maxPage = TypePage.getMaxPageOf(JokeType.getJokeType(type));
		page = page > maxPage ? maxPage : page;
		page = maxPage - page + 1;
		
		
		Criteria criteria = session.createCriteria(TypePage.class);
		criteria.add(Restrictions.eq("page", page));
		criteria.add(Restrictions.eq("type", type));
		
		List<TypePage> typePageList = criteria.list();
		List<Item> items = new ArrayList<Item>();
		
		
		if (typePageList.size() > 0) {
			TypePage typePage = typePageList.get(0);
			List<String> idList = Arrays.asList(typePage.getItems().split(","));
			items = Item.getV2(idList);
		}
		
		HibernateUtil.closeSession(session);
		return items;
	}
	
	
}
