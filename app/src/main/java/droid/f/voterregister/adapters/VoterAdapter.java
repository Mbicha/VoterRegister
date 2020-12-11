package droid.f.voterregister.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import droid.f.voterregister.R;
import droid.f.voterregister.databaseutil.Voter;

import static droid.f.voterregister.Constants.NO_POSITION;

public class VoterAdapter extends RecyclerView.Adapter<VoterAdapter.VoterViewHolder> {
    private List<Voter> mVotersList = new ArrayList<>();
    private OnVoterClickListener listener;

    public void setVoterAdapter(List<Voter> votersList) {
        mVotersList = votersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VoterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voter_item_card, parent, false);
        return new VoterViewHolder(itemView);
    }

    //get position of the Voter object
    public Voter getVoterPosition(int position){
        return mVotersList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VoterViewHolder holder, int position) {
        Voter voter = mVotersList.get(position);

        holder.voterName.setText(voter.getVoterName());
        holder.voterStation.setText(voter.getVoterStaion());
        holder.voterIdNumber.setText(String.valueOf(voter.getVoterId()));
    }

    @Override
    public int getItemCount() {
        return mVotersList.size();
    }


    public class VoterViewHolder extends RecyclerView.ViewHolder{
        private final TextView voterName, voterStation, voterIdNumber;

        public VoterViewHolder(@NonNull View itemView) {
            super(itemView);
            voterName = itemView.findViewById(R.id.name_disp);
            voterStation = itemView.findViewById(R.id.station_disp);
            voterIdNumber = itemView.findViewById(R.id.id_num_disp);

            itemView.setOnClickListener(view ->{
                int currentPosition = getAdapterPosition();
                if (listener != null && currentPosition != RecyclerView.NO_POSITION){
                    listener.onVoterClick(mVotersList.get(currentPosition));
                }
            } );
        }
    }

    public interface OnVoterClickListener{
        void onVoterClick(Voter voter);
    }
    public void setOnClickListener(OnVoterClickListener listener){
        this.listener = listener;
    }

}
