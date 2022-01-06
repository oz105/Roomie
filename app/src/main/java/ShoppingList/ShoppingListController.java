package ShoppingList;


import java.util.List;

public class ShoppingListController{

    public ShoppingListModel ShopListModel;
    public ShoppingListViewActivity ShopListView;


    public ShoppingListController(ShoppingListViewActivity shopList){
        this.ShopListView = shopList;
        this.ShopListModel = new ShoppingListModel(this);
    }

    public String get_item_at_index(int index) {
        return ShopListModel.ListDB.get(index).qty.toString();
    }

    public void editItem(String oldItem, final int index){
        ShopListView.editDialog.setTitle("editing "+oldItem);
        ShopListView.init_edit_item(index);
        //TextView msg = (TextView) ShopListView.editDialog.findViewById(R.id.updateDialog);
        ShopListView.editDialog.show();
    }

    public void init_list_adapter(List<ShopItem> ListDB){
        this.ShopListView.init_list(ListDB);

    }

    public void click_item(int position){

        editItem(ShopListModel.ListDB.get(position).name,position);
    }

    public void newItem(){
        ShopListView.AddDialog.show();
        ShopListView.init_item_add();
    }

    public void load_ShopList(){
        //billsView.progressBar.setVisibility(View.VISIBLE);
        ShopListView.LoadingDialog.show();
        ShopListModel.init_sopping_list();
        ShopListView.LoadingDialog.dismiss();

    }



}

