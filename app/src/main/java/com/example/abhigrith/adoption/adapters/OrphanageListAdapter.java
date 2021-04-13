package com.example.abhigrith.adoption.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abhigrith.adoption.interfaces.OnDocumentCheckListener;
import com.example.abhigrith.adoption.interfaces.OnRecyclerViewItemClickListener;
import com.example.abhigrith.databinding.RecyclerViewOrphanageListItemBinding;
import com.example.abhigrith.orphanage.models.OrphanageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrphanageListAdapter extends FirestoreRecyclerAdapter<OrphanageModel, OrphanageListAdapter.OrphanageViewHolder> {

    private FirestoreRecyclerOptions<OrphanageModel> options;
    private RecyclerViewOrphanageListItemBinding binding;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnDocumentCheckListener onDocumentCheckListener;

    public OrphanageListAdapter(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener, OnDocumentCheckListener onDocumentCheckListener, @NonNull FirestoreRecyclerOptions<OrphanageModel> options) {
        super(options);
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        this.onDocumentCheckListener = onDocumentCheckListener;
        this.options = options;
    }

    @NonNull
    @Override
    public OrphanageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerViewOrphanageListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrphanageViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrphanageViewHolder holder, int position, @NonNull OrphanageModel model) {
        holder.bindOrphanageView(model);
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        if (options.getSnapshots().isEmpty()) {
            onDocumentCheckListener.documentsDonotExist();
        }
    }

    @Override
    public int getItemCount() {
        return this.options.getSnapshots().size();
    }

    public class OrphanageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewOrphanageListItemBinding binding;

        public OrphanageViewHolder(@NonNull RecyclerViewOrphanageListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewItemClickListener.onOrphanageItemClick(getAbsoluteAdapterPosition());
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
