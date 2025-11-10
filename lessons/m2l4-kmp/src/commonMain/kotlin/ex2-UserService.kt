package com.github.watching1981.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class UserService() {
    fun serve(user: User): String
}
