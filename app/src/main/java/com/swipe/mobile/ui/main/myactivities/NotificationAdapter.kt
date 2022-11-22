package com.swipe.mobile.ui.main.myactivities

import android.view.LayoutInflater
import android.view.ViewGroup
import com.swipe.mobile.base.BaseAdapter
import com.swipe.mobile.data.model.Notification
import com.swipe.mobile.databinding.ItemNotificationBinding

class NotificationAdapter: BaseAdapter<ItemNotificationBinding, Notification>() {
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemNotificationBinding
            = ItemNotificationBinding::inflate

    override fun bind(binding: ItemNotificationBinding, item: Notification) {
        with(binding) {
            tvMessage.text = item.message
        }
    }
}