package com.example.abhigrith.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abhigrith.databinding.RecyclerViewParentAuthRequestsForChildBinding;
import com.example.abhigrith.models.ParentsDetailModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class OrphanageParentsChildrenRequestsAdapter extends RecyclerView.Adapter<OrphanageParentsChildrenRequestsAdapter.ParentsViewHolder> {

    private RecyclerViewParentAuthRequestsForChildBinding binding;
    private List<ParentsDetailModel> parents;

    public OrphanageParentsChildrenRequestsAdapter(List<ParentsDetailModel> parents) {
        this.parents = parents;
    }

    @NonNull
    @Override
    public ParentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerViewParentAuthRequestsForChildBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ParentsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentsViewHolder holder, int position) {
        holder.bindParentsData(parents.get(position));
    }

    @Override
    public int getItemCount() {
        return parents.size();
    }

    public class ParentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewParentAuthRequestsForChildBinding binding;

        public ParentsViewHolder(RecyclerViewParentAuthRequestsForChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bindParentsData(ParentsDetailModel model) {
            binding.tvParentProfileName01.setText(model.getFirstParent().getFullName());
            binding.tvParentProfileGender01.setText(model.getFirstParent().getGender());
            binding.tvParentProfileDob01.setText(model.getFirstParent().getDateOfBirth());
            binding.tvParentProfileIncome01.setText(String.valueOf(model.getFirstParent().getIncome()));

            binding.tvParentProfileName02.setText(model.getSecondParent().getFullName());
            binding.tvParentProfileGender02.setText(model.getSecondParent().getGender());
            binding.tvParentProfileDob02.setText(model.getSecondParent().getDateOfBirth());
            binding.tvParentProfileIncome02.setText(String.valueOf(model.getSecondParent().getIncome()));

            binding.tvParentProfilePrimaryAddress.setText(model.getAddress().getPrimaryAddress());
            binding.tvParentProfileSecondaryAddress.setText(model.getAddress().getSecondaryAddress());
            binding.tvParentProfileCity.setText(model.getAddress().getCity());
            binding.tvParentProfileDistrict.setText(model.getAddress().getDistrict());
            binding.tvParentProfilePincode.setText(model.getAddress().getPincode());
            binding.tvParentProfileState.setText(model.getAddress().getState());

            binding.tvParentProfilePrimaryPhone.setText(model.getPrimaryContactNumber());
            binding.tvParentProfileSecondaryPhone.setText(model.getSecondaryContactNumber());
        }

        @Override
        public void onClick(View v) {
            String coupleName = parents.get(getAbsoluteAdapterPosition()).getFirstParent().getFullName() + " and " + parents.get(getAbsoluteAdapterPosition()).getSecondParent().getFullName();
            Snackbar.make(v, "You are currently viewing this couple " + coupleName, Snackbar.LENGTH_LONG).show();
        }
    }
}

