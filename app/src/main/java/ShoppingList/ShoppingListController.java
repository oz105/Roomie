package ShoppingList;


public class ShoppingListController{

    public ShoppingListModel ShopListModel;
    public ShoppingListViewActivity ShopListView;


    public ShoppingListController(ShoppingListViewActivity shopList){
        this.ShopListView = shopList;
        this.ShopListModel = new ShoppingListModel(this);
    }

    public void finish_adding_new_item() {
        //billsView.progressBar.setVisibility(View.VISIBLE);
        //List<String> participateUid = new ArrayList<>();
        //for (int i = 0; i < billsView.newBillListView.getCount(); i++) {
        //    CheckBox cb = (CheckBox) (billsView.newBillListView.getChildAt(i).findViewById(R.id.check));
        //    if(cb.isChecked()){
        //        participateUid.add(names.get(i).uid);
        //    }
        //}
        //double amount = Double.valueOf(billsView.newBillAmount.getText().toString());
        //model.calculate_new_bill(participateUid,amount);
        // refresh db

    }

    public void editItem(String oldItem, final int index){
        ShopListView.editDialog.setTitle("editing "+oldItem);
        ShopListView.init_edit_item(index);
        //TextView msg = (TextView) ShopListView.editDialog.findViewById(R.id.updateDialog);
        ShopListView.editDialog.show();
    }

    public void newItem(){
        ShopListView.AddDialog.show();
        ShopListView.init_item_add();
        finish_adding_new_item();
    }

    public void load_ShopList(){
        //billsView.progressBar.setVisibility(View.VISIBLE);
        ShopListView.LoadingDialog.show();
        ShopListModel.load_shop_list();
        ShopListView.LoadingDialog.dismiss();

    }



}

