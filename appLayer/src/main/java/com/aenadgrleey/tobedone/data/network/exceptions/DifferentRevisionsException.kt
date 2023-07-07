package com.aenadgrleey.tobedone.data.network.exceptions

class DifferentRevisionsException : Exception("Local and remote databases have different revisions(versions)")