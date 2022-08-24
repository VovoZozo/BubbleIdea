package com.example.bubbleidea.helpers

class EmptyQueryException : Exception("Try to add empty value to database")
class DuplicateException : Exception("Try to add duplicate value to database")