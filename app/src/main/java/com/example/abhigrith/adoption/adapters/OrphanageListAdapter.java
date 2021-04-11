package com.example.abhigrith.adoption.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abhigrith.databinding.RecyclerViewOrphanageListItemBinding;
import com.example.abhigrith.orphanage.models.OrphanageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrphanageListAdapter extends FirestoreRecyclerAdapter<OrphanageModel,OrphanageListAdapter.OrphanageViewHolder> {

    private FirestoreRecyclerOptions<OrphanageModel> options;
    private RecyclerViewOrphanageListItemBinding binding;

    public OrphanageListAdapter(@NonNull FirestoreRecyclerOptions<OrphanageModel> options) {
        super(options);
        this.options = options;
    }

    @NonNull
    @Override
    public OrphanageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerViewOrphanageListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrphanageViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrphanageViewHolder holder, int position, @NonNull OrphanageModel model) {
        holder.bindOrphanageView(model);
    }

    @Override
    public int getItemCount() {
        return this.options.getSnapshots().size();
    }

    public static class OrphanageViewHolder extends RecyclerView.ViewHolder{

        private RecyclerViewOrphanageListItemBinding binding;

        public OrphanageViewHolder(@NonNull RecyclerViewOrphanageListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindOrphanageView(OrphanageModel model) {
            binding.tvOrphanageName.setText(model.getOrphanageName());
            binding.tvOrphanageCity.setText(model.getOrphanageAddress().getCity());
            binding.tvOrphanagePincode.setText(model.getOrphanageAddress().getPincode());
            binding.tvOrphanageState.setText(model.getOrphanageAddress().getState());
            binding.tvOrphanageEmail.setText(model.getOrphanageEmail());
            binding.tvOrphanagePrimaryPhone.setText(model.getOrphanagePrimaryPhoneNumber());
            binding.tvOrphanageSecondaryPhone.setText(model.getOrphanageSecondaryPhoneNumber());
        }
    }
}
