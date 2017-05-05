package SpatialQueryProcessing;

public class Keyword {
	String[] KeywordList = {"Restaurant", "Laundry", "Bookstore", "Pharmacy", "Bakery"};
	
	public int getKeywordSize() {
		return KeywordList.length;
	}
	
	public String getKeyword(int num) {
		return KeywordList[num];
	}
	
	public String[] getKeywordList() {
		return KeywordList;
	}
}
