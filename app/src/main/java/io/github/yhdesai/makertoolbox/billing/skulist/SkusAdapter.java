/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.yhdesai.makertoolbox.billing.skulist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.yhdesai.makertoolbox.R;
import io.github.yhdesai.makertoolbox.billing.billing.BillingProvider;
import io.github.yhdesai.makertoolbox.billing.skulist.row.RowViewHolder;
import io.github.yhdesai.makertoolbox.billing.skulist.row.SkuRowData;

/**
 * Adapter for a RecyclerView that shows SKU details for the app.
 * <p>
 *     Note: It's done fragment-specific logic independent and delegates control back to the
 *     specified handler (implemented inside AcquireFragment in this example)
 * </p>
 */
public class SkusAdapter extends RecyclerView.Adapter<RowViewHolder>
        implements RowViewHolder.OnButtonClickListener {
    private List<SkuRowData> mListData;
    private BillingProvider mBillingProvider;

    public SkusAdapter(BillingProvider billingProvider) {
        mBillingProvider = billingProvider;
    }

    void updateData(List<SkuRowData> data) {
        mListData = data;
        notifyDataSetChanged();
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.billing_sku_details_row, parent, false);
        return new RowViewHolder(item, this);
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        SkuRowData data = getData(position);
        if (data != null) {
            holder.title.setText(data.getTitle());
            holder.description.setText(data.getDescription());
            holder.price.setText(data.getPrice());
            holder.button.setEnabled(true);
        }
        switch (data.getSku()) {
            case "gas":
                holder.skuIcon.setImageResource(R.drawable.gas_icon);
                break;
            case "premium":
                holder.skuIcon.setImageResource(R.drawable.premium_icon);
                break;
            case "gold_monthly":
            case "gold_yearly":
                holder.skuIcon.setImageResource(R.drawable.gold_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public void onButtonClicked(int position) {
        SkuRowData data = getData(position);
        mBillingProvider.getBillingManager().startPurchaseFlow(data.getSku(),
                data.getBillingType());

    }

    private SkuRowData getData(int position) {
        return mListData == null ? null : mListData.get(position);
    }
}

