package com.example.juwelierbehrendt.Logic;

import android.widget.TextView;

import com.example.juwelierbehrendt.EntitiesAndValueObjects.Product;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ViewsInitilizer
{
    private TextView tVDisplayName;
    private TextView tVDisplayKind;
    private TextView tVDisplayBrand;
    private TextView tVDisplayAmount;
    private TextView tVDisplayPrice;
    private TextView tVDisplayDiscount;
    private TextView tVDisplayDescription;
    public void initilize(Product product)
    {
        tVDisplayName.setText(product.getName());
        tVDisplayKind.setText(product.getWhat());
        tVDisplayBrand.setText(product.getBrand());
        tVDisplayAmount.setText(String.valueOf(product.getAmount()));
        tVDisplayPrice.setText(String.valueOf(product.getPrice()));
        tVDisplayDiscount.setText(String.valueOf(product.getDiscount()));
        tVDisplayDescription.setText(product.getDescription());
    }
}
