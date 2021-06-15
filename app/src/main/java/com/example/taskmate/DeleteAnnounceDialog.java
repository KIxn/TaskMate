package com.example.taskmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class DeleteAnnounceDialog extends DialogFragment {

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.ConfirmAssDelete))
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Deleting Assignment");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Context context = getContext();

                        HttpUrl url = new HttpUrl.Builder()
                                .scheme("https")
                                .host("lamp.ms.wits.ac.za")
                                .addPathSegment("home").addPathSegment("s2307935").addPathSegment("deleteAnn.php")
                                .addQueryParameter("ann_id",getTag()).build();
                        Request request = new Request.Builder()
                                .url(url).get().build();
                        PhpReq phpReq = new PhpReq();
                        phpReq.sendRequest(getActivity(), request, new RequestHandler() {
                            @Override
                            public void processResponse(String resp) {
                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setMessage(resp);
                                alertDialog.setCancelable(true);
                                alertDialog.show();
                            }
                        });
                        progressDialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(requireContext(), "Announcement Unaffected", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }
}
