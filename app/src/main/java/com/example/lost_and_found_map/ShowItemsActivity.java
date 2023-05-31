package com.example.lost_and_found_map;

import java.util.List;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


public class ShowItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerVItems;
    private ItemAdapter AdapterItem;
    private LostFoundDAO DAO;
    private List<LostFoundItem> Item_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        recyclerVItems = findViewById(R.id.recyclerViewItems);
        recyclerVItems.setLayoutManager(new LinearLayoutManager(this));

        // New instance of databse of operations
        DAO = new LostFoundDAO(this);
        DAO.open();

        // Getting all items from the datbase
        Item_List = DAO.getAllItems();

        // Setting the adapters
        AdapterItem = new ItemAdapter(Item_List, DAO);
        recyclerVItems.setAdapter(AdapterItem);

        // Setting up the listeners
        AdapterItem.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Retrieve the selected item
                LostFoundItem selectedItem = Item_List.get(position);

                // Displays item details and whether it is found
                String details = "Name: " + selectedItem.getName() +
                        "\nDescription: " + selectedItem.getDescription() +
                        "\nContact Number: " + selectedItem.getPhone() +
                        "\nDate Lost: " + selectedItem.getDate();

                if (selectedItem.getPostType().equals("FOUND") || selectedItem.getPostType().equals("Found")) {
                    details ="Name: " + selectedItem.getName() +
                            "\nDescription: " + selectedItem.getDescription() +
                            "\nItem Found Location: " + selectedItem.getLocation() +
                            "\nDate Found: " + selectedItem.getDate();
                }

                Toast.makeText(ShowItemsActivity.this, details, Toast.LENGTH_LONG).show();
            }

            // Deleltes items when button clicked
            @Override
            public void onRemoveButtonClick(int position) {

                LostFoundItem item = Item_List.get(position);
                DAO.deleteItem(item);
                Item_List.remove(position);
                AdapterItem.notifyItemRemoved(position);
            }
        });
    }

    // Closes the database connection
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DAO.close();
    }
}
