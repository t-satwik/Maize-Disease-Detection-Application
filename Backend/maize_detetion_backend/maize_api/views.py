
from rest_framework import status

# from maize_api.serializers import DataSerializer
from maize_api.models import Data
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
import sys 
# class DataViewSet(viewsets.ModelViewSet):
#    queryset = Data.objects.all()
#    serializer_class = DataSerializer

@api_view(['POST'])
def setData(request):
    try:
        data = request.data
        data_obj = Data()
        data_obj.timestamp = data['timestamp']
        data_obj.latitude = data['latitude']
        data_obj.longitude = data['longitude']
        data_obj.predicted_class = data['predicted_class']
        # data_obj.image = data['image']
        data_obj.save()
        return Response({"message":"Data Sent"}, status=status.HTTP_200_OK )
    except Exception:
        print(sys.exc_info())
        return Response({"message":"Bad Request - python Exception"}, status=status.HTTP_400_BAD_REQUEST )