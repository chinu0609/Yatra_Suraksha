from django.shortcuts import render
from rest_framework.decorators import api_view
from rest_framework.response import Response

@api_view(['GET'])
def index(request):
    courses = {
        'course_name':'python',
        'learn' : ['Flask','Django','Tornado','FastApi'],
        'courrse_provider': 'Scaler'
    }
    return Response(courses)