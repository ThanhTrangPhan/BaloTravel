package com.example.balotravel;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateNewJouneyActivity extends AppCompatActivity {

    protected EditText editText;
    protected ArrayList <String> placeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_jouney);
        editText = findViewById(R.id.placeInput);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setInitialQuery(editText.getText().toString()).setCountry("VN").setHint("Hồ Hoàn Kiếm,...").build(CreateNewJouneyActivity.this);
                startActivityForResult(intent, 100);
            }
        });
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBto2FuliFbIADOWHcH3Sf5LcR85_Mjbns");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK ) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            editText.setText(place.getName());
            placeList.add(place.getName());
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerPopupDetailView, new PopupDetailDialogFragment()).commit();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

class ProductListViewAdapter extends BaseAdapter {

    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<String> listProduct;

    ProductListViewAdapter(ArrayList<String> listProduct) {
        this.listProduct = listProduct;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listProduct.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
//        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
//        //Nếu null cần tạo mới
//
//        View viewProduct;
//        if (convertView == null) {
//            viewProduct = View.inflate(parent.getContext(), R.layout.product_view, null);
//        } else viewProduct = convertView;
//
//        //Bind sữ liệu phần tử vào View
//        Product product = (Product) getItem(position);
//        ((TextView) viewProduct.findViewById(R.id.idproduct)).setText(String.format("ID = %d", product.productID));
//        ((TextView) viewProduct.findViewById(R.id.nameproduct)).setText(String.format("Tên SP : %s", product.name));
//        ((TextView) viewProduct.findViewById(R.id.priceproduct)).setText(String.format("Giá %d", product.price));
//
//
//        return viewProduct;
//    }
}