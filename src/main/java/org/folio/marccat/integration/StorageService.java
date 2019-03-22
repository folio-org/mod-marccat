
      if (!(dao instanceof PublisherDescriptorDAO)) {
        if (!descriptorsList.isEmpty() ) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
          int headingNumber = createOrReplaceDescriptor(configuration, descriptor, view);
          headingNumberList.add(headingNumber);
          heading.setKeyNumber(headingNumber);
