package com.example.abhigrith.parent.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.abhigrith.R;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.RecyclerViewParentChildAdoptionListItemBinding;
import com.example.abhigrith.enums.Gender;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ParentSelectedChildrenListAdapter extends RecyclerView.Adapter<ParentSelectedChildrenListAdapter.ChildViewHolder2> {

    private RecyclerViewParentChildAdoptionListItemBinding binding;
    private List<ChildModel> children;

    public ParentSelectedChildrenListAdapter(List<ChildModel> children) {
        this.children = children;
    }

    @NonNull
    @Override
    public ChildViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerViewParentChildAdoptionListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ChildViewHolder2(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder2 holder, int position) {
        holder.bindChildData(children.get(position));
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public class ChildViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewParentChildAdoptionListItemBinding binding;

        public ChildViewHolder2(RecyclerViewParentChildAdoptionListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bindChildData(ChildModel model){
            if(model.getChildGender().equals(Gender.MALE.getGender())){
                Glide.with(itemView)
                        .load(model.getChildImageUrl())
                        .error(R.drawable.male_child_profile)
                        .into(binding.ivParentAdoptionRequestChildProfilePic);
            } else {
                Glide.with(itemView)
                        .load(model.getChildImageUrl())
                        .error(R.drawable.female_child_profile)
                        .into(binding.ivParentAdoptionRequestChildProfilePic);
            }

            binding.tvParentAdoptionRequestChildFullName.setText(model.getChildFullName());
            binding.tvParentAdoptionRequestChildDateOfGender.setText(model.getChildGender());
            binding.tvParentAdoptionRequestChildDateOfBirth.setText(model.getChildDateOfBirth());
        }

        @Override
        public void onClick(View v) {
            Snackbar.make(v,"You have selected " + children.get(getAbsoluteAdapterPosition()).getChildFullName() + " from orphanage",Snackbar.LENGTH_LONG);
        }
    }
}
