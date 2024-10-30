package com.example.presentation.ui.dialog

import androidx.fragment.app.FragmentManager

object ChooseDialogFactory {
    operator fun invoke(
        type: ChooseDialogType,
        fragmentManager: FragmentManager,
        onDismiss: () -> Unit
    ) {
        when (type) {
            ChooseDialogType.Logout -> {
                ChooseDialog(
                    ChooseItem(
                        title = "로그아웃",
                        content = "로그아웃 하시겠습니까?",
                        negativeString = "취소",
                        positiveString = "로그아웃"
                    ),
                    dismissCallback = onDismiss
                ).show(fragmentManager, "ChooseDialog")
            }

            ChooseDialogType.WithDraw -> {
                ChooseDialog(
                    ChooseItem(
                        title = "회원탈퇴",
                        content = "회원탈퇴 하시겠습니까?",
                        negativeString = "취소",
                        positiveString = "탈퇴"
                    ),
                    dismissCallback = {
                        WithdrawDialog(
                            ChooseItem(
                                title = "회원탈퇴",
                                content = "",
                                negativeString = "취소",
                                positiveString = "탈퇴"
                            ),
                            dismissCallback = onDismiss
                        ).show(fragmentManager, "WithdrawDialog")
                    }
                ).show(fragmentManager, "ChooseDialog")
            }
        }
    }
}

sealed interface ChooseDialogType {
    data object WithDraw : ChooseDialogType
    data object Logout : ChooseDialogType
}