package com.example.abhigrith.orphanage.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abhigrith.R;
import com.example.abhigrith.databinding.RecyclerViewParentAuthRequestsForChildBinding;
import com.example.abhigrith.parent.models.ParentsDetailModel;
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
            binding.tvParentProfileName01.setText(itemView.getContext().getString(R.string.parent_name_01,model.getFirstParent().getFullName()));
            binding.tvParentProfileGender01.setText(itemView.getContext().getString(R.string.parent_gender_01,model.getFirstParent().getGender()));
            binding.tvParentProfileDob01.setText(itemView.getContext().getString(R.string.parent_date_of_birth_01,model.getFirstParent().getDateOfBirth()));
            binding.tvParentProfileIncome01.setText(itemView.getContext().getString(R.string.parent_income_01,String.valueOf(model.getFirstParent().getIncome())));

            binding.tvParentProfileName02.setText(itemView.getContext().getString(R.string.parent_name_02,model.getSecondParent().getFullName()));
            binding.tvParentProfileGender02.setText(itemView.getContext().getString(R.string.parent_gender_02,model.getSecondParent().getGender()));
            binding.tvParentProfileDob02.setText(itemView.getContext().getString(R.string.parent_date_of_birth_02,model.getSecondParent().getDateOfBirth()));
            binding.tvParentProfileIncome02.setText(itemView.getContext().getString(R.string.parent_income_02,String.valueOf(model.getSecondParent().getIncome())));

            binding.tvParentProfilePrimaryAddress.setText(itemView.getContext().getString(R.string.parent_primary_address,model.getAddress().getPrimaryAddress()));
            binding.tvParentProfileSecondaryAddress.setText(itemView.getContext().getString(R.string.parent_secondary_address,model.getAddress().getSecondaryAddress()));
            binding.tvParentProfileCity.setText(itemView.getContext().getString(R.string.parent_city,model.getAddress().getCity()));
            binding.tvParentProfileDistrict.setText(itemView.getContext().getString(R.string.parent_district,model.getAddress().getDistrict()));
            binding.tvParentProfilePincode.setText(itemView.getContext().getString(R.string.parent_pincode,model.getAddress().getPincode()));
            binding.tvParentProfileState.setText(itemView.getContext().getString(R.string.parent_state,model.getAddress().getState()));

            binding.tvParentProfilePrimaryPhone.setText(itemView.getContext().getString(R.string.parent_primary_phone_number,model.getPrimaryContactNumber()));
            binding.tvParentProfileSecondaryPhone.setText(itemView.getContext().getString(R.string.parent_secondary_phone_number,model.getSecondaryContactNumber()));
        }

        @Override
        public void onClick(View v) {
            String coupleName = parents.get(getAbsoluteAdapterPosition()).getFirstParent().getFullName() + " and " + parents.get(getAbsoluteAdapterPosition()).getSecondParent().getFullName();
            Snackbar.make(v, "You are currently viewing this couple " + coupleName, Snackbar.LENGTH_LONG).show();
        }
    }
}

