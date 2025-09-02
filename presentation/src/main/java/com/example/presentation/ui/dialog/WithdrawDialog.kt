package com.example.presentation.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.presentation.MainActivity
import com.example.presentation.R
import com.example.presentation.base.BaseDialogFragment
import com.example.presentation.databinding.DialogWithdrawBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WithdrawDialog(
    private val chooseItem: ChooseItem,
    private val cancelable: Boolean = true,
    private val dismissCallback: () -> Unit = {}
) : BaseDialogFragment<DialogWithdrawBinding>(R.layout.dialog_withdraw) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = cancelable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item = chooseItem
            tvDismiss.setOnClickListener {
                withdraw()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun withdraw() {

        firebaseAuth.currentUser?.let {
            firebaseAuth.signInWithEmailAndPassword(
                it.email.toString(),
                binding.password.text.toString()
            ).addOnSuccessListener {
                firebaseAuth.currentUser?.delete()?.addOnSuccessListener {
                    Toast.makeText(requireContext(), "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    dismissCallback.invoke()
                    dismiss()

                    startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                        putExtra(KEY_LOGIN_DIRECT, true)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                }?.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "회원탈퇴를 실패하였습니다: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "로그인된 사용자가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val KEY_LOGIN_DIRECT = "key_login_direct"
    }
}

