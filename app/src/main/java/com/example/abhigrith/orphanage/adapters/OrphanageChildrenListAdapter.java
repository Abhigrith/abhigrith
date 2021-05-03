package com.example.abhigrith.orphanage.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.abhigrith.R;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.RecyclerViewParentChildAdoptionListItemBinding;
import com.example.abhigrith.enums.Gender;
import com.example.abhigrith.orphanage.activities.OrphanageParentsAdoptionRequestsForChildrenActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;

public class OrphanageChildrenListAdapter extends FirestoreRecyclerAdapter<ChildModel, OrphanageChildrenListAdapter.ChildViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private static final String CHILD_MODEL = "childData";

    private FirestoreRecyclerOptions<ChildModel> options;

    public OrphanageChildrenListAdapter(@NonNull FirestoreRecyclerOptions<ChildModel> options) {
        super(options);
        this.options = options;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewParentChildAdoptionListItemBinding binding = RecyclerViewParentChildAdoptionListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChildViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChildViewHolder holder, int position, @NonNull ChildModel model) {
        holder.bindChildData(model);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewParentChildAdoptionListItemBinding binding;

        public ChildViewHolder(@NonNull RecyclerViewParentChildAdoptionListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String childId = options.getSnapshots().get(getAbsoluteAdapterPosition()).getChildId();
            String childFullName = options.getSnapshots().get(getAbsoluteAdapterPosition()).getChildFullName();

            ChildModel child = options.getSnapshots().get(getAbsoluteAdapterPosition());

            Intent intent = new Intent(itemView.getContext(), OrphanageParentsAdoptionRequestsForChildrenActivity.class);
            intent.putExtra(CHILD_MODEL,child);

            itemView.getContext().startActivity(intent);
            Toast.makeText(itemView.getContext().getApplicationContext(), "You are currently viewing child : " + childFullName, Toast.LENGTH_LONG).show();
        }

        private void bindChildData(ChildModel model) {
            binding.tvParentAdoptionRequestChildFullName.setText(model.getChildFullName());
            binding.tvParentAdoptionRequestChildGender.setText(model.getChildGender());
            binding.tvParentAdoptionRequestChildDateOfBirth.setText(model.getChildDateOfBirth());

            if (model.getChildGender().equals(Gender.MALE.getGender())) {
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
        }
    }
}
