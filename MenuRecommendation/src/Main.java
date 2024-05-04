public class Main {
    public static void main(String[] args) {
        MenuRecommendation menu = new MenuRecommendation();
        menu.addItem(1, "Biryani");
        menu.addItem(2, "Chicken 65");
        menu.addItem(3, "Chicken Macaroni");
        menu.outOfStockItem(1);
        menu.rateItem(1, 5);
        menu.rateItem(1, 5);
        menu.rateItem(2, 5);
        System.out.println(menu.getRecommendedItem().toString());
    }
}